package com.example.f1_app.features.news.data.datasource

import com.example.f1_app.features.news.data.api.NewsApiService
import com.example.f1_app.features.news.data.api.dto.NewsItemDto

class NewsRemoteDataSource(
    private val api: NewsApiService
) {
    suspend fun fetchNews(): List<NewsItemDto> = api.getLatestNews()
}
