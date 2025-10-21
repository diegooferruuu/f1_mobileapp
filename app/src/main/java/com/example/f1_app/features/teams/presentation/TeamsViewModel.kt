package com.example.f1_app.features.teams.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.teams.domain.model.Team
import com.example.f1_app.features.teams.domain.usecase.GetTeamsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TeamsUiState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val error: String? = null
)

class TeamsViewModel(
    private val getTeamsUseCase: GetTeamsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState(isLoading = true))
    val uiState: StateFlow<TeamsUiState> = _uiState

    fun loadTeams() {
        _uiState.value = TeamsUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val teams = getTeamsUseCase()
                _uiState.value = TeamsUiState(isLoading = false, teams = teams)
            } catch (e: Exception) {
                _uiState.value = TeamsUiState(isLoading = false, error = e.message)
            }
        }
    }
}
