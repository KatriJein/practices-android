package com.example.practicesandroid.drivers.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverDetailsUIModel(
    val id: String?,
    val name: String?,
    val surname: String?,
    val nationality: String?,
    val birthday: String?,
    val number: Int?,
    val team: Team?,
    val points: Int?,
    val position: Int?,
    val wins: Int?,
    val results: List<RaceResultUIModel>
)

@Serializable
data class RaceResultUIModel(
    val race: RaceUIModel,
    val result: ResultUIModel?,
    val sprintResult: ResultUIModel?
)

@Serializable
data class RaceUIModel(
    val id: String,
    val name: String,
    val round: Int,
    val date: String,
    val circuit: CircuitUIModel
)

@Serializable
data class CircuitUIModel(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val length: Int,
    val lapRecord: String,
    val firstParticipationYear: Int,
    val numberOfCorners: Int,
    val fastestLapYear: Int?,
    val fastestLapDriverId: String?,
    val fastestLapTeamId: String?
)

@Serializable
data class ResultUIModel(
    val finishingPosition: String?,
    val gridPosition: Int?,
    val raceTime: String?,
    val pointsObtained: Int?,
    val retired: Boolean?,
)