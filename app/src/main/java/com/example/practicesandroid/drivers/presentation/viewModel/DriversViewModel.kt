package com.example.practicesandroid.drivers.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.DriverDetails
import com.example.practicesandroid.drivers.domain.interactor.DriversInteractor
import com.example.practicesandroid.drivers.domain.model.DriverEntity
import com.example.practicesandroid.drivers.presentation.model.DriverUIModel
import com.example.practicesandroid.drivers.presentation.model.DriversViewState
import com.example.practicesandroid.drivers.presentation.model.Team
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.core.launchLoadingAndError
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

class DriversViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: DriversInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow(DriversViewState())
    val state = mutableState.asStateFlow()

    init {
        loadDrivers()
    }

    fun onRetryClick() = loadDrivers()

    fun onDriverClick(driver: DriverUIModel) {
        topLevelBackStack.add(DriverDetails(
            driverId = driver.id ?: "",
            initialPoints = driver.points,
            initialPosition = driver.position,
            initialWins = driver.wins
        ))
    }

    private fun loadDrivers() {
        viewModelScope.launchLoadingAndError(
            handleError = { e ->
                updateState(DriversViewState.State.Error(e.localizedMessage.orEmpty()))
            }
        ) {
            updateState(DriversViewState.State.Loading)

            val drivers = interactor.getDrivers()
            updateState(DriversViewState.State.Success(mapToUi(drivers)))
        }
    }

    private fun updateState(state: DriversViewState.State) =
        mutableState.update { it.copy(state = state) }

    private fun mapToUi(drivers: List<DriverEntity>): List<DriverUIModel> = drivers.map { driver ->
        DriverUIModel(
            id = driver.id,
            name = driver.name,
            surname = driver.surname,
            nationality = driver.nationality,
            birthday = driver.birthday?.format(dateFormatter),
            number = driver.number,
            team = Team(
                id = driver.team?.id,
                name = driver.team?.name,
                nationality = driver.team?.nationality,
                year = driver.team?.year
            ),
            points = driver.points,
            position = driver.position,
            wins = driver.wins,
        )
    }
}