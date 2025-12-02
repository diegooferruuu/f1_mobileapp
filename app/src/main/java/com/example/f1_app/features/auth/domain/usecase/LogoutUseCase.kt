package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}

