package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.model.User
import com.example.f1_app.features.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): User? {
        return repository.getCurrentUser()
    }
}

