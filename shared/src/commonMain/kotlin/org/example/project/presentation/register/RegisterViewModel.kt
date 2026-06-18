package org.example.project.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.RegisterUseCase

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val pass: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

sealed class RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PassChanged(val pass: String) : RegisterEvent()
    data class TermsAcceptedChanged(val accepted: Boolean) : RegisterEvent()
    object Submit : RegisterEvent()
}

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> _state.update { it.copy(name = event.name) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is RegisterEvent.PassChanged -> _state.update { it.copy(pass = event.pass) }
            is RegisterEvent.TermsAcceptedChanged -> _state.update { it.copy(termsAccepted = event.accepted) }
            RegisterEvent.Submit -> submitData()
        }
    }

    private fun submitData() {
        val currentState = _state.value
        if (!currentState.termsAccepted) {
            _state.update { it.copy(errorMessage = "Debes aceptar los términos y condiciones.") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val result = registerUseCase(currentState.name, currentState.email, currentState.pass)
                if (result.isSuccess) {
                    _state.update { it.copy(isLoading = false, success = true) }
                } else {
                    _state.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error al registrar") }
            }
        }
    }
}
