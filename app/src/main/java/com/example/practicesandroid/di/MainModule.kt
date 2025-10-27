package com.example.practicesandroid.di

import com.example.practicesandroid.Drivers
import com.example.practicesandroid.drivers.presentation.viewModel.DriversDetailsViewModel
import com.example.practicesandroid.drivers.presentation.viewModel.DriversViewModel
import com.example.practicesandroid.navigation.Route
import com.example.practicesandroid.navigation.TopLevelBackStack
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Drivers) }
}