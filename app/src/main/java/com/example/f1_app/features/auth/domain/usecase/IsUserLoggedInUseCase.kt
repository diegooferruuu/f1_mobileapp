package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.repository.AuthRepository

class IsUserLoggedInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}

