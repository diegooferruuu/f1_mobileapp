package com.example.f1_app.features.drivers.data.api

import com.example.f1_app.features.drivers.data.api.dto.DriverDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DriversApiService {
    @GET("v1/drivers")
    suspend fun getDrivers(@Query("session_key") session: Int): List<DriverDto>
}
