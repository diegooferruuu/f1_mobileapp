package com.example.f1_app.features.drivers.UnitTests

import com.example.f1_app.features.drivers.data.datasource.DriverRemoteDataSource
import com.example.f1_app.features.drivers.data.repository.DriverRepositoryImpl
import com.example.f1_app.features.drivers.domain.model.Driver
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DriverRepositoryImplTest {

    private val remote = mockk<DriverRemoteDataSource>()
    private val repository = DriverRepositoryImpl(remote)

    @Test
    fun `getDrivers delegates to remote datasource`() = runTest {
        val mockList = listOf(mockk<Driver>())
        coEvery { remote.getDrivers(10033) } returns mockList

        val result = repository.getDrivers(10033)

        assertEquals(mockList, result)
    }
}
