package com.example.practicesandroid.drivers.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverUIModel(
    val id: String?,
    val name: String?,
    val surname: String?,
    val nationality: String?,
    val birthday: String?,
    val number: Int?,
    val team: Team?,
    val points: Int?,
    val position: Int?,
    val wins: Int?
)
