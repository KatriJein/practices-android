package com.example.practicesandroid.account.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDriverUIModel(
    val id: String,
    val name: String,
    val surname: String,
    val number: Int?,
    val teamName: String?,
    val points: Int?,
    val position: Int?,
    val wins: Int?,
    val nationality: String?,
    val birthday: String?
)