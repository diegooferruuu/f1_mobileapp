package com.example.f1_app.features.news.domain.usecase

import com.example.f1_app.features.news.domain.model.NewsItem
import com.example.f1_app.features.news.domain.repository.NewsRepository

class GetNewsUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): List<NewsItem> = repository.getNews()
}
