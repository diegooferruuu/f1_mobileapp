package com.example.f1_app.features.drivers.data.datasource

import com.example.f1_app.features.drivers.data.api.DriversApiService
import com.example.f1_app.features.drivers.domain.model.Driver

class DriverRemoteDataSource(
    private val service: DriversApiService
) {
    suspend fun getDrivers(session: Int): List<Driver> =
        service.getDrivers(session).map { dto ->
            Driver(
                id = dto.id ?: 0,
                name = dto.name ?: "Unknown",
                image = dto.image ?: "",
                nationality = dto.nationality ?: "",
                abbr = dto.abbr ?: "",
                number = dto.number ?: 0,
                teamName = dto.teamName ?: ""
            )
        }
}
