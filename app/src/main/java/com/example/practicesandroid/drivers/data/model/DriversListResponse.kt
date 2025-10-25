package com.example.practicesandroid.drivers.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class DriversListResponse(
    val drivers_championship: List<DriverItem>?
)

@Keep
@Serializable
class DriverItem(
    val classificationId: Int?,
    val driverId: String?,
    val teamId: String?,
    val points: Int?,
    val position: Int?,
    val wins: Int?,
    val driver: Driver?,
    val team: Team?
)

@Keep
@Serializable
class Driver(
    val name: String?,
    val surname: String?,
    val nationality: String?,
    val birthday: String?,
    val number: Int?,
    val shortName: String?,
    val url: String?
)

@Keep
@Serializable
class Team(
    val teamId: String?,
    val teamName: String?,
    val teamNationality: String?,
    val firstAppeareance: Int?,
    val constructorsChampionships: Int?,
    val driversChampionships: Int?,
    val url: String?
)