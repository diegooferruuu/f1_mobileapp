package com.example.f1_app.features.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.news.domain.model.NewsItem
import com.example.f1_app.features.news.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = false,
    val news: List<NewsItem> = emptyList(),
    val error: String? = null
)

class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState(isLoading = true))
    val uiState: StateFlow<NewsUiState> = _uiState

    fun loadNews() {
        _uiState.value = NewsUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val items = getNewsUseCase()
                _uiState.value = NewsUiState(isLoading = false, news = items)
            } catch (e: Exception) {
                _uiState.value = NewsUiState(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }
}
