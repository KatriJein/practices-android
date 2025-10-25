package com.example.practicesandroid.drivers.data.repository

import com.example.practicesandroid.drivers.data.api.DriversApi
import com.example.practicesandroid.drivers.data.mapper.DriversResponseToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DriversRepository(
    private val api: DriversApi,
    private val mapper: DriversResponseToEntityMapper,
) {
    suspend fun getDrivers() = withContext(Dispatchers.IO) {
        val response = api.getDrivers()
        mapper.mapResponse(response)
    }

    suspend fun getDriverById(driverId: String) = withContext(Dispatchers.IO) {
        val response = api.getDriverById(driverId)
        mapper.mapDriverDetailsResponse(response)
    }
}