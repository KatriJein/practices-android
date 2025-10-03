package com.example.practicesandroid.drivers.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.drivers.presentation.MockData
import com.example.practicesandroid.drivers.presentation.model.DriverDetailsViewState
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriversDetailsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val driverId: String
) : ViewModel() {
    private val mutableState = MutableStateFlow(DriverDetailsViewState())
    val state = mutableState.asStateFlow()

    init {
        loadDriver()
    }

    private fun loadDriver() {
        viewModelScope.launch {
            val driver = MockData.getDriverById(driverId)

            mutableState.value = mutableState.value.copy(
                driver = driver
            )
        }
    }
}