package com.example.practicesandroid.drivers.presentation.model

data class DriversViewState(
    val state: State = State.Loading,
) {
    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Success(val data: List<DriverUIModel>) : State
    }
}