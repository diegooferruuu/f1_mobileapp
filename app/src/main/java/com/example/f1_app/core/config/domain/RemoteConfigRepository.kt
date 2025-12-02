package com.example.f1_app.core.config.domain

interface RemoteConfigRepository {
    suspend fun getString(key: String): String
    suspend fun getBoolean(key: String): Boolean
    suspend fun getLong(key: String): Long
    suspend fun getDouble(key: String): Double
    suspend fun fetchAndActivate(): Boolean
}

