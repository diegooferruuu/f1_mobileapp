package com.example.f1_app.features.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.calendar.domain.model.Meeting
import com.example.f1_app.features.calendar.domain.usecase.GetMeetingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Month

data class CalendarUiState(
    val isLoading: Boolean = false,
    val months: Map<Month, List<Meeting>> = emptyMap(),
    val error: String? = null
)

class CalendarViewModel(
    private val getMeetings: GetMeetingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState(isLoading = true))
    val uiState: StateFlow<CalendarUiState> = _uiState

    fun load(year: Int = 2025) {
        _uiState.value = CalendarUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val meetings = getMeetings(year)
                val grouped = meetings.groupBy { it.dateStart.month }
                _uiState.value = CalendarUiState(isLoading = false, months = grouped)
            } catch (e: Exception) {
                _uiState.value = CalendarUiState(isLoading = false, error = e.message)
            }
        }
    }
}
