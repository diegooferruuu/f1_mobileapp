package com.example.f1_app.features.results.domain.usecase

import com.example.f1_app.features.results.domain.model.Session
import com.example.f1_app.features.results.domain.repository.ResultsRepository

class SearchSessionsUseCase(private val repository: ResultsRepository) {
    suspend operator fun invoke(
        location: String? = null,
        year: Int? = null,
        sessionType: String? = null,
        sessionName: String? = null
    ): List<Session> = repository.searchSessions(location, year, sessionType, sessionName)
}
