package com.example.f1_app.features.teams.data.repository

import com.example.f1_app.features.teams.data.datasource.TeamsRemoteDataSource
import com.example.f1_app.features.teams.domain.model.Team
import com.example.f1_app.features.teams.domain.repository.TeamsRepository

class TeamsRepositoryImpl(
    private val remoteDataSource: TeamsRemoteDataSource
) : TeamsRepository {

    override suspend fun getTeams(): List<Team> {
        val teamsDto = remoteDataSource.getTeams() // ya es una lista filtrada
        return teamsDto.map {
            Team(
                id = it.id ?: 0,
                name = it.name ?: "Unknown",
                logo = it.logo ?: "",
                base = it.base ?: "N/A",
                director = it.director ?: "N/A"
            )
        }
    }
}
