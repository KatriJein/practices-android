package com.example.practicesandroid.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.core.navigation.Drivers
import com.example.core.navigation.Route
import com.example.core.navigation.TopLevelBackStack
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Drivers) }
    single {
        getDataStore(androidContext())
    }
}

fun getDataStore(androidContext: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }