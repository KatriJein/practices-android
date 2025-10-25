package com.example.practicesandroid.drivers.di

import com.example.practicesandroid.drivers.data.mapper.DriversResponseToEntityMapper
import com.example.practicesandroid.drivers.data.repository.DriversRepository
import com.example.practicesandroid.drivers.domain.interactor.DriversInteractor
import com.example.practicesandroid.drivers.presentation.viewModel.DriversDetailsViewModel
import com.example.practicesandroid.drivers.presentation.viewModel.DriversViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import com.example.practicesandroid.drivers.data.api.DriversApi

val driversFeatureModule = module {
    single { get<Retrofit>().create(DriversApi::class.java) }

    factory { DriversResponseToEntityMapper() }
    single { DriversRepository(get(), get()) }

    single { DriversInteractor(get()) }

    viewModel { DriversViewModel(get(), get()) }
    viewModel { params ->
        DriversDetailsViewModel(
            interactor = get(),
            driverId = params.get(),
            initialPoints = params.getOrNull(),
            initialPosition = params.getOrNull(),
            initialWins = params.getOrNull()
        )
    }
}