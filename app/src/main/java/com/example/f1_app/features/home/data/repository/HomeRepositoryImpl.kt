package com.example.f1_app.features.home.data.repository

import com.example.f1_app.features.home.data.datasource.HomeRemoteDataSource
import com.example.f1_app.features.home.domain.model.HomeOverview
import com.example.f1_app.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {

    override suspend fun getOverview(): HomeOverview {
        return remoteDataSource.getOverview()
    }
}

