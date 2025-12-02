package com.example.f1_app.features.teams.data.datasource

import com.example.f1_app.features.teams.data.api.TeamsApiService
import com.example.f1_app.features.teams.data.api.dto.TeamDto

open class TeamsRemoteDataSource(
    private val api: TeamsApiService
) {
    suspend fun getTeams(): List<TeamDto> {
        val response = api.getTeams()
        return response.response.filter { it.base != null }
    }}
