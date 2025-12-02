package com.example.f1_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.auth.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = logoutUseCase()
                if (result.isSuccess) {
                    onLogoutSuccess()
                }
            } catch (e: Exception) {
                // Silent fail - could add error handling if needed
            }
        }
    }
}

