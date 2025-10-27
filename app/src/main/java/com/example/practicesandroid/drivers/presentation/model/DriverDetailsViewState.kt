package com.example.practicesandroid.drivers.presentation.model

data class DriverDetailsViewState(
    val state: State = State.Loading
) {
    sealed class State {
        object Loading : State()
        data class Success(val driver: DriverDetailsUIModel) : State()
        data class Error(val message: String) : State()
    }
}