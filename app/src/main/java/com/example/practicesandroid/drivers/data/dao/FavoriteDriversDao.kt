package com.example.practicesandroid.drivers.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicesandroid.drivers.data.entity.FavoriteDriverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDriversDao {
    @Query("SELECT * FROM favorite_drivers")
    fun getAll(): Flow<List<FavoriteDriverEntity>>

    @Query("SELECT * FROM favorite_drivers WHERE id = :driverId")
    suspend fun getById(driverId: String): FavoriteDriverEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: FavoriteDriverEntity)

    @Delete
    suspend fun delete(driver: FavoriteDriverEntity)

    @Query("DELETE FROM favorite_drivers WHERE id = :driverId")
    suspend fun deleteById(driverId: String)

    @Query("SELECT EXISTS(SELECT * FROM favorite_drivers WHERE id = :driverId)")
    suspend fun isFavorite(driverId: String): Boolean
}