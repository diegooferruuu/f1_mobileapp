package com.example.f1_app.features.news.data.api

import com.example.f1_app.features.news.data.api.dto.NewsItemDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface NewsApiService {

    @Headers(
        "x-rapidapi-host: f1-latest-news.p.rapidapi.com",
        "x-rapidapi-key: 3fe7754b80mshb5d06f5a77a76a7p1a473bjsnaf8b746d4082"
    )
    @GET("news")
    suspend fun getLatestNews(): List<NewsItemDto>
}
