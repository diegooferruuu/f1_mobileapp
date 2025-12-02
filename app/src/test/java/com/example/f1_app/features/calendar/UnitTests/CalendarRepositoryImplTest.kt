package com.example.f1_app.features.calendar.UnitTests

import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import com.example.f1_app.features.calendar.data.repository.CalendarRepositoryImpl
import com.example.f1_app.features.calendar.domain.model.Meeting
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class CalendarRepositoryImplTest {

    private val remote = mockk<CalendarRemoteDataSource>()
    private val repository = CalendarRepositoryImpl(remote)

    @Test
    fun `getMeetings delegates to remote`() = runTest {
        val mockList = listOf(mockk<Meeting>())
        coEvery { remote.getMeetings(2025) } returns mockList

        val result = repository.getMeetings(2025)

        assertEquals(mockList, result)
    }
}
