package com.example.practicesandroid.account.di

import com.example.practicesandroid.account.data.repository.FavoriteDriversRepository
import com.example.practicesandroid.account.data.repository.ProfileRepository
import com.example.practicesandroid.account.domain.interactor.ProfileInteractor
import com.example.practicesandroid.account.presentation.viewModel.AccountViewModel
import com.example.practicesandroid.account.presentation.viewModel.EditAccountViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val accountFeatureModule = module {
    single { FavoriteDriversRepository(get()) }
    single { ProfileRepository(get()) }

    single { ProfileInteractor(get()) }

    viewModel { AccountViewModel(get(), get(), get()) }
    viewModel { EditAccountViewModel(get()) }
}