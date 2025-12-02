package com.example.f1_app.features.auth.data.datasource

import com.example.f1_app.features.auth.domain.model.User

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun signUp(email: String, password: String, displayName: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): User?
}

