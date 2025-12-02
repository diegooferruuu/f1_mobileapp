package com.example.f1_app.features.results.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.results.domain.model.Session
import com.example.f1_app.features.results.domain.model.SessionResult
import com.example.f1_app.features.results.domain.usecase.GetSessionResultsUseCase
import com.example.f1_app.features.results.domain.usecase.SearchSessionsUseCase
import com.example.f1_app.features.results.domain.usecase.GetFastestLapUseCase
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import com.example.f1_app.features.drivers.domain.model.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ResultsUiState(
    val isLoading: Boolean = false,
    val sessions: List<Session> = emptyList(),
    val selectedSessionKey: Int? = null,
    val results: List<SessionResult> = emptyList(),
    val resultsEnriched: List<ResultRow> = emptyList(),
    val fastestLap: FastLapDisplay? = null,
    val nonClassified: List<String> = emptyList(),
    val error: String? = null,
    val filters: ResultsFilters = ResultsFilters(),
    val activeTab: SessionTab = SessionTab.Race,
    val headerTitle: String = "",
    val headerSub: String = ""
)

data class ResultsFilters(
    val location: String? = null,
    val year: Int? = 2025,
    val sessionType: String? = null
)

class ResultsViewModel(
    private val searchSessions: SearchSessionsUseCase,
    private val getSessionResults: GetSessionResultsUseCase,
    private val getDrivers: GetDriversUseCase,
    private val getFastestLap: GetFastestLapUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState

    fun updateFilters(location: String?, year: Int?, sessionType: String?) {
        _uiState.value = _uiState.value.copy(filters = ResultsFilters(location, year, sessionType))
    }

    fun search() {
        val f = _uiState.value.filters
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val sessionNameParam = when (f.sessionType) {
                    "Race" -> "Race"
                    "Qualifying" -> "Qualifying"
                    else -> null
                }
                val sessions = searchSessions(f.location, f.year, f.sessionType, sessionNameParam)
                val now = java.time.OffsetDateTime.now()
                val filtered = sessions.filter { it.dateStart.isBefore(now) || it.dateStart.isEqual(now) }
                _uiState.value = _uiState.value.copy(isLoading = false, sessions = filtered, selectedSessionKey = null, results = emptyList(), resultsEnriched = emptyList())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun loadResults(sessionKey: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, selectedSessionKey = sessionKey, error = null)
        viewModelScope.launch {
            try {
                val r = getSessionResults(sessionKey)
                val drivers = getDrivers(sessionKey)
                val byNumber = drivers.associateBy { it.number }
                val enriched = r.map { sr ->
                    val d: Driver? = byNumber[sr.driverNumber]
                    ResultRow(
                        position = sr.position,
                        driverNumber = sr.driverNumber,
                        driverName = d?.name ?: "#${sr.driverNumber}",
                        team = d?.teamName,
                        points = sr.points ?: 0,
                        gapToLeader = sr.gapToLeaderSeconds,
                        laps = sr.numberOfLaps,
                        duration = sr.durationSeconds
                    )
                }
                val header = _uiState.value.sessions.find { it.sessionKey == sessionKey }
                val fastest = getFastestLap(sessionKey)
                val fastestDisplay = fastest?.let { fl ->
                    val d = byNumber[fl.driverNumber]
                    FastLapDisplay(
                        driver = d?.name ?: "#${fl.driverNumber}",
                        team = d?.teamName ?: "",
                        time = formatLapTime(fl.totalTimeSeconds)
                    )
                }
                val nonClassifiedList = r.filter { it.dnf || it.dns || it.dsq }.map { nc ->
                    val d = byNumber[nc.driverNumber]
                    (d?.name ?: "#${nc.driverNumber}") + when {
                        nc.dnf -> " - DNF"
                        nc.dns -> " - DNS"
                        nc.dsq -> " - DSQ"
                        else -> ""
                    }
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    results = r,
                    resultsEnriched = enriched,
                    headerTitle = header?.location ?: "",
                    headerSub = listOfNotNull(header?.sessionName, header?.countryName).joinToString(" • "),
                    fastestLap = fastestDisplay,
                    nonClassified = nonClassifiedList
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun setTab(tab: SessionTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedSessionKey = null, results = emptyList(), resultsEnriched = emptyList(), fastestLap = null, nonClassified = emptyList())
    }

    fun loadInitialLatestRace(currentYear: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, activeTab = SessionTab.Race, selectedSessionKey = null)
        viewModelScope.launch {
            try {
                val sessions = searchSessions(location = null, year = currentYear, sessionType = SessionTab.Race.apiValue, sessionName = "Race")
                _uiState.value = _uiState.value.copy(isLoading = false, sessions = sessions, selectedSessionKey = null, results = emptyList(), resultsEnriched = emptyList())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

enum class SessionTab(val label: String, val apiValue: String?) {
    Race("CARRERA", "Race"),
    Qualifying("CLASIFICACIÓN", "Qualifying")
}

data class ResultRow(
    val position: Int?,
    val driverNumber: Int,
    val driverName: String?,
    val team: String?,
    val points: Int,
    val gapToLeader: Double?,
    val laps: Int?,
    val duration: Double?
)

data class FastLapDisplay(
    val driver: String,
    val team: String,
    val time: String
)

private fun formatLapTime(seconds: Double): String {
    val mins = (seconds / 60).toInt()
    val secs = seconds - mins * 60
    return String.format("%d:%06.3f", mins, secs)
}
