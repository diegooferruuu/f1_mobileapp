package com.example.f1_app.features.teams.domain.model

data class Team(
    val id: Int? = 0,
    val name: String? = "Sin nombre",
    val logo: String? = "Sin logo",
    val base: String? = "Sin base",
    val director: String?= "Sin director"
)
