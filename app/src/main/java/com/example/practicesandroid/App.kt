package com.example.practicesandroid

import android.app.Application
import com.example.practicesandroid.account.di.accountFeatureModule
import com.example.practicesandroid.di.dbModule
import com.example.practicesandroid.di.mainModule
import com.example.practicesandroid.di.networkModule
import com.example.practicesandroid.drivers.di.driversFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, networkModule, driversFeatureModule, accountFeatureModule, dbModule)
        }
    }
}