package com.example.f1_app.features.calendar.data.repository

import com.example.f1_app.features.calendar.data.datasource.CalendarRemoteDataSource
import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository

class CalendarRepositoryImpl(
    private val remote: CalendarRemoteDataSource
) : CalendarRepository {
    override suspend fun getMeetings(year: Int): List<Meeting> = remote.getMeetings(year)
}
