package com.example.practicesandroid.drivers.domain.model

import java.time.LocalDateTime

data class DriverEntity(
    val id: String?,
    val name: String?,
    val surname: String?,
    val nationality: String?,
    val birthday: LocalDateTime?,
    val number: Int?,
    val team: TeamEntity?,
    val points: Int?,
    val position: Int?,
    val wins: Int?
)