package com.example.f1_app.features.calendar.domain.usecase

import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.repository.CalendarRepository

class GetMeetingsUseCase(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(year: Int): List<Meeting> = repository.getMeetings(year)
}
