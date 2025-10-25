package com.example.practicesandroid.drivers.domain.model

import java.time.LocalDateTime

data class RaceResultEntity(
    val race: RaceEntity,
    val result: ResultEntity?,
    val sprintResult: ResultEntity?
)

data class RaceEntity(
    val id: String,
    val name: String,
    val round: Int,
    val date: LocalDateTime,
    val circuit: CircuitEntity
)

data class CircuitEntity(
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

data class ResultEntity(
    val finishingPosition: String?,
    val gridPosition: Int?,
    val raceTime: String?,
    val pointsObtained: Int?,
    val retired: Boolean?
)