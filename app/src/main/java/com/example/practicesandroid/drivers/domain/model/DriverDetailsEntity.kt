package com.example.practicesandroid.drivers.domain.model

data class DriverDetailsEntity(
    val driver: DriverEntity?,
    val results: List<RaceResultEntity>,
)