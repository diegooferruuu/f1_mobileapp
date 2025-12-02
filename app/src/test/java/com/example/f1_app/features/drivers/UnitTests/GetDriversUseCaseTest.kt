package com.example.f1_app.features.drivers.UnitTests

import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.repository.DriverRepository
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDriversUseCaseTest {

    private val repo = mockk<DriverRepository>()
    private val useCase = GetDriversUseCase(repo)

    @Test
    fun `invoke calls repository and returns drivers`() = runTest {
        val expected = listOf(mockk<Driver>())
        coEvery { repo.getDrivers(10033) } returns expected

        val result = useCase(10033)

        assertEquals(expected, result)
    }
}
