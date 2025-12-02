package com.example.f1_app.features.auth.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null
)

