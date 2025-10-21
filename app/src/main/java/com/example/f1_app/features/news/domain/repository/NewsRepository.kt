package com.example.f1_app.features.news.domain.repository

import com.example.f1_app.features.news.domain.model.NewsItem

interface NewsRepository {
    suspend fun getNews(): List<NewsItem>
}
