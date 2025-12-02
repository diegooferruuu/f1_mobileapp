package com.example.f1_app.features.home.domain.model

data class HomeOverview(
    val racesCompleted: Int = 0,
    val driversOnPodium: Int = 0,
    val safetyCars: Int = 0,
    val nextRace: String = "",
    val weather: String = "",
    val firstPractice: String = ""
)

