package com.example.practicesandroid.drivers.presentation.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Driver(
    val id: String,
    val name: String,
    val surname: String,
    val nationality: String,
    val birthday: LocalDate,
    val number: Int,
    val team: Team,
    val points: Int,
    val position: Int,
    val wins: Int
)
