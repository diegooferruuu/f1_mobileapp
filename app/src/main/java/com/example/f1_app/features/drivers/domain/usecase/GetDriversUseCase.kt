package com.example.f1_app.features.drivers.domain.usecase

import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.repository.DriverRepository

class GetDriversUseCase(
    private val repository: DriverRepository
) {
    suspend operator fun invoke(session: Int): List<Driver> = repository.getDrivers(session)
}
