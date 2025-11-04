package com.example.practicesandroid.drivers.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practicesandroid.drivers.data.dao.FavoriteDriversDao
import com.example.practicesandroid.drivers.data.entity.FavoriteDriverEntity

@Database(
    entities = [FavoriteDriverEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDriversDao(): FavoriteDriversDao
}