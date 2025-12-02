package com.example.f1_app.features.home.data.datasource

import com.example.f1_app.features.home.domain.model.HomeOverview

interface HomeRemoteDataSource {
    suspend fun getOverview(): HomeOverview
}

