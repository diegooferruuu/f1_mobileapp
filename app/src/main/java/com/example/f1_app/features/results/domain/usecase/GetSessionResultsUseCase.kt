package com.example.f1_app.features.results.domain.usecase

import com.example.f1_app.features.results.domain.model.SessionResult
import com.example.f1_app.features.results.domain.repository.ResultsRepository

class GetSessionResultsUseCase(private val repository: ResultsRepository) {
    suspend operator fun invoke(sessionKey: Int): List<SessionResult> = repository.getSessionResults(sessionKey)
}
