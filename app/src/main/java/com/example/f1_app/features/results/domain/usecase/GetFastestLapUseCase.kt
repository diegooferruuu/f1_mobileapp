package com.example.f1_app.features.results.domain.usecase

import com.example.f1_app.features.results.domain.model.FastLap
import com.example.f1_app.features.results.domain.repository.ResultsRepository

class GetFastestLapUseCase(private val repository: ResultsRepository) {
    suspend operator fun invoke(sessionKey: Int): FastLap? = repository.getFastestLap(sessionKey)
}
