package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.model.User
import com.example.f1_app.features.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}

