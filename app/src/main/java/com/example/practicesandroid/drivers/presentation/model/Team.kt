package com.example.practicesandroid.drivers.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String,
    val name: String,
    val nationality: String,
    val year: Int
)
