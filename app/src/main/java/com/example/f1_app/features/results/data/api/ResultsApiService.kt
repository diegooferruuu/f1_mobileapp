package com.example.f1_app.features.results.data.api

import com.example.f1_app.features.results.data.api.dto.LapDto
import com.example.f1_app.features.results.data.api.dto.SessionDto
import com.example.f1_app.features.results.data.api.dto.SessionResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ResultsApiService {
    @GET("v1/sessions")
    suspend fun getSessions(
        @Query("location") location: String? = null,
        @Query("year") year: Int? = null,
        @Query("session_type") sessionType: String? = null,
        @Query("session_name") sessionName: String? = null
    ): List<SessionDto>

    @GET("v1/session_result")
    suspend fun getSessionResults(@Query("session_key") sessionKey: Int): List<SessionResultDto>

    @GET("v1/laps")
    suspend fun getLaps(@Query("session_key") sessionKey: Int): List<LapDto>
}
