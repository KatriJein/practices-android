package com.example.practicesandroid.drivers.presentation.viewModel

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.core.launchLoadingAndError
import com.example.practicesandroid.drivers.domain.interactor.DriversInteractor
import com.example.practicesandroid.drivers.domain.model.DriverDetailsEntity
import com.example.practicesandroid.drivers.domain.model.RaceResultEntity
import com.example.practicesandroid.drivers.presentation.model.CircuitUIModel
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsUIModel
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsViewState
import com.example.practicesandroid.drivers.presentation.model.RaceResultUIModel
import com.example.practicesandroid.drivers.presentation.model.RaceUIModel
import com.example.practicesandroid.drivers.presentation.model.ResultUIModel
import com.example.practicesandroid.drivers.presentation.model.Team
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class DriversDetailsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: DriversInteractor,
    private val driverId: String,
    private val initialPoints: Int? = null,
    private val initialPosition: Int? = null,
    private val initialWins: Int? = null
) : ViewModel() {
    private val mutableState = MutableStateFlow(DriverDetailsViewState())
    val state = mutableState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val raceDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    init {
        loadDriverDetails()
        checkIsFavorite()
    }

    fun onRetryClick() = loadDriverDetails()

    fun onBackClick() {
        topLevelBackStack.removeLast()
    }

    fun onShareClick(context: Context) {
        val currentState = state.value.state
        if (currentState is DriverDetailsViewState.State.Success) {
            shareDriverInfo(context, currentState.driver)
        }
    }

    private fun shareDriverInfo(context: Context, driver: DriverDetailsUIModel) {
        val shareText = buildString {
            append("Информация о пилоте Формулы 1\n\n")
            append("Пилот: ${driver.name} ${driver.surname}\n")
            driver.number?.let { append("Номер: $it\n") }
            driver.team?.name?.let { append("Команда: $it\n") }
            driver.points?.let { append("Очки: $it\n") }
            driver.position?.let { append("Позиция в чемпионате: $it\n") }
            driver.wins?.let { append("Победы: $it\n") }
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareChooser = Intent.createChooser(shareIntent, "Поделиться информацией о пилоте")
        ContextCompat.startActivity(context, shareChooser, null)
    }

    private fun loadDriverDetails() {
        viewModelScope.launchLoadingAndError(
            handleError = { e ->
                updateState(DriverDetailsViewState.State.Error(e.localizedMessage.orEmpty()))
            }
        ) {
            updateState(DriverDetailsViewState.State.Loading)

            val driverDetails = interactor.getDriverById(driverId)
            updateState(DriverDetailsViewState.State.Success(mapToUi(driverDetails)))
        }
    }

    private fun updateState(state: DriverDetailsViewState.State) =
        mutableState.update { it.copy(state = state) }

    private fun mapToUi(driverDetails: DriverDetailsEntity): DriverDetailsUIModel {
        val driverEntity = driverDetails.driver

        return DriverDetailsUIModel(
            id = driverEntity?.id,
            name = driverEntity?.name,
            surname = driverEntity?.surname,
            nationality = driverEntity?.nationality,
            birthday = driverEntity?.birthday?.format(dateFormatter),
            number = driverEntity?.number,
            team = driverEntity?.team?.let { team ->
                Team(
                    id = team.id,
                    name = team.name,
                    nationality = team.nationality,
                    year = team.year
                )
            },
            points = initialPoints ?: driverEntity?.points,
            position = initialPosition ?: driverEntity?.position,
            wins = initialWins ?: driverEntity?.wins,
            results = mapResultsToUi(driverDetails.results)
        )
    }

    private fun mapResultsToUi(results: List<RaceResultEntity>): List<RaceResultUIModel> {
        return results.map { result ->
            RaceResultUIModel(
                race = RaceUIModel(
                    id = result.race.id,
                    name = result.race.name,
                    round = result.race.round,
                    date = result.race.date.format(raceDateFormatter),
                    circuit = CircuitUIModel(
                        id = result.race.circuit.id,
                        name = result.race.circuit.name,
                        country = result.race.circuit.country,
                        city = result.race.circuit.city,
                        length = result.race.circuit.length,
                        lapRecord = result.race.circuit.lapRecord,
                        firstParticipationYear = result.race.circuit.firstParticipationYear,
                        numberOfCorners = result.race.circuit.numberOfCorners,
                        fastestLapYear = result.race.circuit.fastestLapYear,
                        fastestLapDriverId = result.race.circuit.fastestLapDriverId,
                        fastestLapTeamId = result.race.circuit.fastestLapTeamId
                    )
                ),
                result = result.result?.let { res ->
                    ResultUIModel(
                        finishingPosition = res.finishingPosition,
                        gridPosition = res.gridPosition,
                        raceTime = res.raceTime,
                        pointsObtained = res.pointsObtained,
                        retired = res.retired,
                    )
                },
                sprintResult = result.sprintResult?.let { sprint ->
                    ResultUIModel(
                        finishingPosition = sprint.finishingPosition,
                        gridPosition = sprint.gridPosition,
                        raceTime = sprint.raceTime,
                        pointsObtained = sprint.pointsObtained,
                        retired = sprint.retired,
                    )
                }
            )
        }
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            val currentState = state.value.state
            if (currentState is DriverDetailsViewState.State.Success) {
                interactor.toggleFavorite(currentState.driver)
                checkIsFavorite()
            }
        }
    }

    private fun checkIsFavorite() {
        viewModelScope.launch {
            _isFavorite.value = interactor.isFavorite(driverId)
        }
    }
}