package com.example.f1_app.features.auth.data.repository

import com.example.f1_app.features.auth.data.datasource.AuthRemoteDataSource
import com.example.f1_app.features.auth.domain.model.User
import com.example.f1_app.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return remoteDataSource.login(email, password)
    }

    override suspend fun signUp(email: String, password: String, displayName: String): Result<User> {
        return remoteDataSource.signUp(email, password, displayName)
    }

    override suspend fun logout(): Result<Unit> {
        return remoteDataSource.logout()
    }

    override suspend fun getCurrentUser(): User? {
        return remoteDataSource.getCurrentUser()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return remoteDataSource.getCurrentUser() != null
    }
}

