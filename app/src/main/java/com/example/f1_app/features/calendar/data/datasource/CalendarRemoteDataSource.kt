package com.example.f1_app.features.calendar.data.datasource

import com.example.f1_app.features.calendar.data.api.OpenF1Service
import com.example.f1_app.features.calendar.domain.model.Meeting
import java.time.OffsetDateTime

class CalendarRemoteDataSource(
    private val service: OpenF1Service
) {
    suspend fun getMeetings(year: Int): List<Meeting> =
        service.getMeetings(year).map { dto ->
            Meeting(
                meetingKey = dto.meetingKey,
                circuitShortName = dto.circuitShortName,
                meetingCode = dto.meetingCode,
                location = dto.location,
                countryCode = dto.countryCode,
                countryName = dto.countryName,
                meetingName = dto.meetingName,
                dateStart = OffsetDateTime.parse(dto.dateStart),
                year = dto.year
            )
        }
}
