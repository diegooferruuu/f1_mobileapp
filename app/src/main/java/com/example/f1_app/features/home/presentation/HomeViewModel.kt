package com.example.f1_app.features.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.core.config.domain.usecase.FetchRemoteConfigUseCase
import com.example.f1_app.core.config.domain.usecase.GetRemoteConfigStringUseCase
import com.example.f1_app.features.home.domain.model.HomeOverview
import com.example.f1_app.features.home.domain.usecase.GetHomeOverviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeOverviewUseCase: GetHomeOverviewUseCase,
    private val fetchRemoteConfigUseCase: FetchRemoteConfigUseCase,
    private val getRemoteConfigStringUseCase: GetRemoteConfigStringUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _state = MutableStateFlow<HomeOverview?>(null)
    val state: StateFlow<HomeOverview?> = _state

    private val _title = MutableStateFlow("F1 Hub")
    val title: StateFlow<String> = _title

    init {
        loadData()
        loadRemoteConfig()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = getHomeOverviewUseCase()
        }
    }

    private fun loadRemoteConfig() {
        viewModelScope.launch {
            Log.d(TAG, "Loading Remote Config...")

            // Fetch and activate remote config
            val fetchSuccess = fetchRemoteConfigUseCase()
            Log.d(TAG, "Fetch and activate success: $fetchSuccess")

            // Get the title from remote config
            val remoteTitle = getRemoteConfigStringUseCase("title")
            Log.d(TAG, "Remote title retrieved: $remoteTitle")

            _title.value = remoteTitle
            Log.d(TAG, "Title updated to: ${_title.value}")
        }
    }
}

