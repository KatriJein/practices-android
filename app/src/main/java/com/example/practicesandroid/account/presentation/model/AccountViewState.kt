package com.example.practicesandroid.account.presentation.model

data class AccountViewState(
    val state: State = State.Loading,
) {
    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Success(val favouritesDrivers: List<FavoriteDriverUIModel>) : State
    }
}