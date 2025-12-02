package com.example.f1_app.core.config.domain.usecase

import com.example.f1_app.core.config.domain.RemoteConfigRepository

class GetRemoteConfigStringUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(key: String): String {
        return repository.getString(key)
    }
}

