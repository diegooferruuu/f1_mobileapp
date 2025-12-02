package com.example.f1_app.features.teams.data.api

import com.example.f1_app.features.teams.data.api.dto.TeamsResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers

interface TeamsApiService {

    @Headers(
        "x-rapidapi-host: v1.formula-1.api-sports.io",
        "x-rapidapi-key: c905003aa3887d029aa508fbdec8dbd2"
    )
    @GET("teams")
    suspend fun getTeams(): TeamsResponseWrapper
}
