package com.example.practicesandroid.account.di

import com.example.practicesandroid.account.data.repository.FavoriteDriversRepository
import com.example.practicesandroid.account.presentation.viewModel.AccountViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val accountFeatureModule = module {
    single { FavoriteDriversRepository(get()) }

    viewModel { AccountViewModel(get(), get()) }
}