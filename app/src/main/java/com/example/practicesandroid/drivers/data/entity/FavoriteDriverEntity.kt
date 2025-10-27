package com.example.practicesandroid.drivers.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_drivers")
data class FavoriteDriverEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val surname: String,
    val number: Int?,
    val teamName: String?,
    val points: Int?,
    val position: Int?,
    val wins: Int?,
    val nationality: String?,
    val birthday: String?,
)