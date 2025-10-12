package com.example.practicesandroid.drivers.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicesandroid.drivers.presentation.MockData
import com.example.practicesandroid.drivers.presentation.model.DriversViewState
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriversViewModel(private val topLevelBackStack: TopLevelBackStack<Route>) : ViewModel() {
    private val mutableState = MutableStateFlow(DriversViewState())
    val state = mutableState.asStateFlow()

    init {
        loadDrivers()
    }

    private fun loadDrivers() {
        viewModelScope.launch {
            val drivers =  MockData.getDrivers().sortedBy { it.position }

            mutableState.value = mutableState.value.copy(
                drivers = drivers
            )
        }
    }
}