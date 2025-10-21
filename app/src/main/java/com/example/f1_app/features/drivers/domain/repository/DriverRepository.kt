package com.example.f1_app.features.drivers.domain.repository

import com.example.f1_app.features.drivers.domain.model.Driver

interface DriverRepository {
    suspend fun getDrivers(session: Int): List<Driver>
}