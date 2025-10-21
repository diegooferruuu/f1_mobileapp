package com.example.f1_app.features.news.data.repository

import com.example.f1_app.features.news.data.datasource.NewsRemoteDataSource
import com.example.f1_app.features.news.domain.model.NewsItem
import com.example.f1_app.features.news.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val remote: NewsRemoteDataSource
) : NewsRepository {
    override suspend fun getNews(): List<NewsItem> {
        return remote.fetchNews().mapNotNull { dto ->
            val title = dto.title ?: return@mapNotNull null
            val url = dto.url ?: return@mapNotNull null
            val source = dto.source ?: "unknown"
            NewsItem(title = title, url = url, source = source)
        }
    }
}
