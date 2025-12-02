package com.example.f1_app.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1_app.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false
)

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null,
            errorMessage = null
        )
    }

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

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            errorMessage = null
        )
    }

    fun onSignUpClick() {
        if (!validateInputs()) return

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            authRepository.signUp(
                email = _uiState.value.email,
                password = _uiState.value.password,
                displayName = _uiState.value.name
            ).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignUpSuccessful = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = when (exception.message) {
                            "The email address is already in use by another account." ->
                                "Este correo ya está registrado"
                            "The email address is badly formatted." ->
                                "El formato del correo no es válido"
                            "The given password is invalid. [ Password should be at least 6 characters ]" ->
                                "La contraseña debe tener al menos 6 caracteres"
                            else -> "Error al registrarse: ${exception.message}"
                        }
                    )
                }
            )
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        var nameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // Validate name
        if (_uiState.value.name.isBlank()) {
            nameError = "El nombre es requerido"
            isValid = false
        } else if (_uiState.value.name.length < 2) {
            nameError = "El nombre debe tener al menos 2 caracteres"
            isValid = false
        }

        // Validate email
        if (_uiState.value.email.isBlank()) {
            emailError = "El correo es requerido"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            emailError = "Correo electrónico inválido"
            isValid = false
        }

        // Validate password
        if (_uiState.value.password.isBlank()) {
            passwordError = "La contraseña es requerida"
            isValid = false
        } else if (_uiState.value.password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }

        // Validate confirm password
        if (_uiState.value.confirmPassword.isBlank()) {
            confirmPasswordError = "Confirma tu contraseña"
            isValid = false
        } else if (_uiState.value.password != _uiState.value.confirmPassword) {
            confirmPasswordError = "Las contraseñas no coinciden"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )

        return isValid
    }
}
