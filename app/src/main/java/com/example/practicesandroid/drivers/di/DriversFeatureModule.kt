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
import com.example.practicesandroid.drivers.data.cache.BadgeCacheManager
import com.example.practicesandroid.drivers.data.repository.FavoriteDriversRepository

val driversFeatureModule = module {
    single { get<Retrofit>().create(DriversApi::class.java) }

    factory { DriversResponseToEntityMapper() }
    single { DriversRepository(get(), get(), get())  }
    single { FavoriteDriversRepository(get()) }

    single { DriversInteractor(get(), get(),get()) }
    single { BadgeCacheManager() }

    viewModel { DriversViewModel(get(), get()) }
    viewModel { params ->
        DriversDetailsViewModel(
            topLevelBackStack = get(),
            interactor = get(),
            driverId = params.get(),
            initialPoints = params.getOrNull(),
            initialPosition = params.getOrNull(),
            initialWins = params.getOrNull()
        )
    }
}