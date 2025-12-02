package com.example.f1_app.features.drivers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.drivers.domain.model.Driver
import com.example.f1_app.features.drivers.domain.usecase.GetDriversUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class DriverUiState(
    val isLoading: Boolean = false,
    val drivers: List<Driver> = emptyList(),
    val error: String? = null
)

class DriverViewModel(
    private val getDrivers: GetDriversUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriverUiState(isLoading = true))
    open val uiState: StateFlow<DriverUiState> = _uiState

    open fun load(year: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val drivers = getDrivers(10033)
                _uiState.value = _uiState.value.copy(isLoading = false, drivers = drivers)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }
}