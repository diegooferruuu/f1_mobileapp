package com.example.f1_app.core.config.domain.usecase

import com.example.f1_app.core.config.domain.RemoteConfigRepository

class FetchRemoteConfigUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.fetchAndActivate()
    }
}

