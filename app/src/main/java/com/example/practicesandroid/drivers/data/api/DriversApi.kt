package com.example.practicesandroid.drivers.data.api

import com.example.practicesandroid.drivers.data.model.DriverDetailsResponse
import com.example.practicesandroid.drivers.data.model.DriversListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DriversApi {
    @GET("current/drivers-championship")
    suspend fun getDrivers(): DriversListResponse

    @GET("current/drivers/{driverId}")
    suspend fun getDriverById(@Path("driverId") driverId: String): DriverDetailsResponse
}
