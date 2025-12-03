package com.example.profile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.profile.domain.interactor.ProfileInteractor
import com.example.profile.presentation.viewModel.ProfileViewModel
import com.example.profile.presentation.viewModel.EditProfileViewModel
import com.example.profile.data.repository.ProfileRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

val accountFeatureModule = module {
    single { ProfileRepository(get()) }

    single { ProfileInteractor(get()) }

    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get()) }

    single {
        getDataStore(androidContext())
    }
}


fun getDataStore(androidContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("profile")
    }