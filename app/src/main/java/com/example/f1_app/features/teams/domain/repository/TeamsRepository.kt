package com.example.f1_app.features.teams.domain.repository

import com.example.f1_app.features.teams.domain.model.Team

interface TeamsRepository {
    suspend fun getTeams(): List<Team>
}
