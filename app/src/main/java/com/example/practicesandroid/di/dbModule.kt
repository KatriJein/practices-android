package com.example.practicesandroid.di

import android.content.Context
import androidx.room.Room
import com.example.practicesandroid.drivers.data.db.AppDatabase
import org.koin.dsl.module

val dbModule = module {
    single { DatabaseBuilder.getInstance(get()) }
}

object DatabaseBuilder {
    fun getInstance(context: Context) = buildRoomDB(context)

    private fun buildRoomDB(context: Context) = Room.databaseBuilder(
        context.applicationContext, AppDatabase::class.java, "app"
    ).build()
}