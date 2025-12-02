package com.example.f1_app.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )
    }

    fun onLoginClick() {
        if (!validateInputs()) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = loginUseCase(
                email = _uiState.value.email.trim(),
                password = _uiState.value.password
            )

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
            )
        }
    }

    private fun validateInputs(): Boolean {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        var isValid = true

        var emailError: String? = null
        var passwordError: String? = null

        // Validate email
        if (email.isEmpty()) {
            emailError = "El correo es requerido"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Ingresa un correo válido"
            isValid = false
        }

        // Validate password
        if (password.isEmpty()) {
            passwordError = "La contraseña es requerida"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        return isValid
    }

    private fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("password is invalid") == true ||
            exception.message?.contains("no user record") == true ||
            exception.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                "Correo o contraseña incorrectos"
            exception.message?.contains("network") == true ->
                "Error de conexión. Verifica tu internet"
            else -> "Error al iniciar sesión: ${exception.message}"
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

