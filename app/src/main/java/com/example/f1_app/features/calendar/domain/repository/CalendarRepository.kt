package com.example.f1_app.features.calendar.domain.repository

import com.example.f1_app.features.calendar.domain.model.Meeting

interface CalendarRepository {
    suspend fun getMeetings(year: Int): List<Meeting>
}
