package com.example.practicesandroid.account.data.repository

import com.example.practicesandroid.drivers.data.db.AppDatabase
import com.example.practicesandroid.drivers.data.entity.FavoriteDriverEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class FavoriteDriversRepository(
    private val db: AppDatabase
) {
    fun getFavorites(): Flow<List<FavoriteDriverEntity>> =
        db.favoriteDriversDao().getAll()

    suspend fun getFavoriteById(driverId: String) =
        withContext(Dispatchers.IO) {
            db.favoriteDriversDao().getById(driverId)
        }

    suspend fun addToFavorites(driver: FavoriteDriverEntity) =
        withContext(Dispatchers.IO) {
            db.favoriteDriversDao().insert(driver)
        }

    suspend fun removeFromFavorites(driverId: String) =
        withContext(Dispatchers.IO) {
            db.favoriteDriversDao().deleteById(driverId)
        }

    suspend fun isFavorite(driverId: String) =
        withContext(Dispatchers.IO) {
            db.favoriteDriversDao().isFavorite(driverId)
        }

    suspend fun toggleFavorite(driver: FavoriteDriverEntity) {
        withContext(Dispatchers.IO) {
            if (db.favoriteDriversDao().isFavorite(driver.id)) {
                db.favoriteDriversDao().deleteById(driver.id)
            } else {
                db.favoriteDriversDao().insert(driver)
            }
        }
    }
}