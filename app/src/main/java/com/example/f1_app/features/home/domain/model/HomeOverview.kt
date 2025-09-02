package com.example.f1_app.features.home.domain.model

data class HomeOverview(
    val racesCompleted: Int,
    val driversOnPodium: Int,
    val safetyCars: Int,
    val nextRace: String,
    val weather: String,
    val firstPractice: String,
    val newsTitle: String,
    val newsDescription: String
)