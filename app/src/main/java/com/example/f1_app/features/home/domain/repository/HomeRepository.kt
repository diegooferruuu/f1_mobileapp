package com.example.f1_app.features.home.domain.repository

import com.example.f1_app.features.home.domain.model.HomeOverview

interface HomeRepository {
    fun getHomeOverview(): HomeOverview
}