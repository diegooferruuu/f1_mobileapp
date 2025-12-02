package com.example.f1_app.features.calendar.UnitTests

import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository
import com.example.f1_app.features.calendar.domain.usecase.GetMeetingsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class GetMeetingsUseCaseTest {

    private val repo = mockk<CalendarRepository>()
    private val useCase = GetMeetingsUseCase(repo)

    @Test
    fun `invoke uses repository`() = runTest {
        val expected = listOf(mockk<Meeting>())
        coEvery { repo.getMeetings(2025) } returns expected

        val result = useCase(2025)

        assertEquals(expected, result)
    }
}
