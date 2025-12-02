package com.example.f1_app.features.calendar.UnitTests

import com.example.f1_app.features.calendar.data.api.OpenF1Service
import com.example.f1_app.features.calendar.data.api.dto.MeetingDto
import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.OffsetDateTime

class CalendarRemoteDataSourceTest {

    private val service = mockk<OpenF1Service>()
    private val dataSource = CalendarRemoteDataSource(service)

    @Test
    fun `getMeetings should map DTO to domain correctly`() = runTest {
        val dto = MeetingDto(
            meetingKey = 1,
            circuitShortName = "Monza",
            meetingCode = "ITA",
            location = "Monza",
            countryCode = "IT",
            countryName = "Italy",
            meetingName = "Gran Premio de Italia",
            dateStart = "2025-09-07T14:00:00Z",
            year = 2025
        )

        coEvery { service.getMeetings(2025) } returns listOf(dto)

        val result = dataSource.getMeetings(2025)

        assertEquals(1, result.size)
        val meeting = result.first()

        assertEquals(dto.meetingKey, meeting.meetingKey)
        assertEquals(dto.circuitShortName, meeting.circuitShortName)
        assertEquals(OffsetDateTime.parse(dto.dateStart), meeting.dateStart)
    }
}
