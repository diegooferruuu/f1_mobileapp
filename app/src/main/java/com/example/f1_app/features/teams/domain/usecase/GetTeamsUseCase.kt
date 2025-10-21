package com.example.f1_app.features.teams.domain.usecase

import com.example.f1_app.features.teams.domain.repository.TeamsRepository

class GetTeamsUseCase(
    private val repository: TeamsRepository
) {
    suspend operator fun invoke() = repository.getTeams()
}
