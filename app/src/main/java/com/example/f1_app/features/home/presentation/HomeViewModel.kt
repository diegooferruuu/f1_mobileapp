package com.example.f1_app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.home.domain.model.HomeOverview
import com.example.f1_app.features.home.domain.usecase.GetHomeOverviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeOverviewUseCase: GetHomeOverviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeOverview?>(null)
    val state: StateFlow<HomeOverview?> = _state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = getHomeOverviewUseCase()
        }
    }
}