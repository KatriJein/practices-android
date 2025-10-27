package com.example.practicesandroid.account.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.DriverDetails
import com.example.practicesandroid.account.presentation.model.AccountViewState
import com.example.practicesandroid.account.presentation.model.FavoriteDriverUIModel
import com.example.practicesandroid.drivers.domain.interactor.DriversInteractor
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: DriversInteractor,
) : ViewModel() {
    private val mutableState = MutableStateFlow(AccountViewState())
    val state: StateFlow<AccountViewState> = mutableState.asStateFlow()

    init {
        loadFavorites()
    }

    fun onDriverClick(driver: FavoriteDriverUIModel) {
        topLevelBackStack.add(
            DriverDetails(
                driverId = driver.id ?: "",
                initialPoints = driver.points,
                initialPosition = driver.position,
                initialWins = driver.wins
            )
        )
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                interactor.getFavorites().collect { favorites ->
                    val uiModels = favorites.map { favoriteEntity ->
                        FavoriteDriverUIModel(
                            id = favoriteEntity.id,
                            name = favoriteEntity.name,
                            surname = favoriteEntity.surname,
                            number = favoriteEntity.number,
                            teamName = favoriteEntity.teamName,
                            points = favoriteEntity.points,
                            position = favoriteEntity.position,
                            wins = favoriteEntity.wins,
                            nationality = favoriteEntity.nationality,
                            birthday = favoriteEntity.birthday
                        )
                    }

                    updateState(AccountViewState.State.Success(uiModels))
                }
            } catch (e: Exception) {
                updateState(AccountViewState.State.Error(e.message ?: "Unknown error"))
            }
        }
    }

    private fun updateState(newState: AccountViewState.State) {
        mutableState.update { it.copy(state = newState) }
    }
}

