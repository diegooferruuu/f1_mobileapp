package com.example.f1_app.features.auth.domain.usecase

import com.example.f1_app.features.auth.domain.model.User
import com.example.f1_app.features.auth.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, displayName: String): Result<User> {
        return repository.signUp(email, password, displayName)
    }
}

