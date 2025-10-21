package com.example.f1_app.features.drivers.data.repository

import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository
import com.example.f1_app.features.drivers.data.datasource.DriverRemoteDataSource
import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.repository.DriverRepository

class DriverRepositoryImpl (
    private val remote: DriverRemoteDataSource
): DriverRepository {
    override suspend fun getDrivers(session: Int): List<Driver> = remote.getDrivers(session)
}


