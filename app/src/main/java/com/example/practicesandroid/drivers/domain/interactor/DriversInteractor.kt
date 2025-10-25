package com.example.practicesandroid.drivers.domain.interactor

import com.example.practicesandroid.drivers.data.repository.DriversRepository

class DriversInteractor(
    private val repository: DriversRepository
) {
    suspend fun getDrivers() = repository.getDrivers()

    suspend fun getDriverById(driverId: String) = repository.getDriverById(driverId)
}