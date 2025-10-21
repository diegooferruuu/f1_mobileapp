package com.example.f1_app.features.teams.data.datasource

import com.example.f1_app.features.teams.data.api.TeamsApiService
import com.example.f1_app.features.teams.data.api.dto.TeamDto

class TeamsRemoteDataSource(
    private val api: TeamsApiService
) {
    suspend fun getTeams(): List<TeamDto> {
        val response = api.getTeams()
        // Filtramos los equipos que tienen base nula
        return response.response.filter { it.base != null }
    }}
