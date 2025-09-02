package com.example.f1_app.features.home.domain.usecase

import com.example.f1_app.features.home.domain.model.HomeOverview
import com.example.f1_app.features.home.domain.repository.HomeRepository

class GetHomeOverviewUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): HomeOverview {
        return repository.getHomeOverview()
    }
}