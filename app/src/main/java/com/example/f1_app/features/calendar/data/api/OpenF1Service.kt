package com.example.f1_app.features.calendar.data.api

import com.example.f1_app.features.calendar.data.api.dto.MeetingDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenF1Service {
    @GET("v1/meetings")
    suspend fun getMeetings(@Query("year") year: Int): List<MeetingDto>
}
