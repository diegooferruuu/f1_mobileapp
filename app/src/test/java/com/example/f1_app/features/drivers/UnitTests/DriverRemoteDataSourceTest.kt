package com.example.f1_app.features.drivers.UnitTests

import com.example.f1_app.features.drivers.data.api.DriversApiService
import com.example.f1_app.features.drivers.data.api.dto.DriverDto
import com.example.f1_app.features.drivers.data.datasource.DriverRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DriverRemoteDataSourceTest {

    private val service = mockk<DriversApiService>()
    private val dataSource = DriverRemoteDataSource(service)

    @Test
    fun `getDrivers maps DTO to domain Driver correctly`() = runTest {
        val dto = DriverDto(
            id = 44,
            name = "Lewis Hamilton",
            image = "https://example.com/hamilton.jpg",
            nationality = "British",
            abbr = "HAM",
            number = 44,
            teamName = "Mercedes"
        )

        coEvery { service.getDrivers(10033) } returns listOf(dto)

        val result = dataSource.getDrivers(10033)

        assertEquals(1, result.size)
        val driver = result.first()
        assertEquals(44, driver.id)
        assertEquals("Lewis Hamilton", driver.name)
        assertEquals("https://example.com/hamilton.jpg", driver.image)
        assertEquals("British", driver.nationality)
        assertEquals("HAM", driver.abbr)
        assertEquals(44, driver.number)
        assertEquals("Mercedes", driver.teamName)
    }

    @Test
    fun `getDrivers handles nulls in DTO with defaults`() = runTest {
        val dto = DriverDto(
            id = null,
            name = null,
            image = null,
            nationality = null,
            abbr = null,
            number = null,
            teamName = null
        )

        coEvery { service.getDrivers(10033) } returns listOf(dto)

        val result = dataSource.getDrivers(10033)

        assertEquals(1, result.size)
        val driver = result.first()
        // Default mapping in your datasource sets 0 or empty/default strings
        assertEquals(0, driver.id)
        assertEquals("Unknown", driver.name)
        assertEquals("", driver.image)
        assertEquals("", driver.nationality)
        assertEquals("", driver.abbr)
        assertEquals(0, driver.number)
        assertEquals("", driver.teamName)
    }
}
