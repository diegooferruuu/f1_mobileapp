package com.example.f1_app.features.home.data.repository

import com.example.f1_app.features.home.domain.model.HomeOverview
import com.example.f1_app.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl : HomeRepository {
    override fun getHomeOverview(): HomeOverview {
        return HomeOverview(
            racesCompleted = 14,
            driversOnPodium = 9,
            safetyCars = 17,
            nextRace = "Monza",
            weather = "Sunny",
            firstPractice = "Friday",
            newsTitle = "Latest News & Summary",
            newsDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut."
        )
    }
}