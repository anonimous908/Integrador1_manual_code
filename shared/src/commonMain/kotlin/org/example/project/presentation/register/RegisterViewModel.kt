package org.example.project.presentation.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.User
import org.example.project.domain.usecase.LoginWithGoogleUseCase
import org.example.project.domain.usecase.RegisterUseCase
import org.example.project.presentation.base.BaseViewModel

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val pass: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val success: Boolean = false
)

sealed class RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PassChanged(val pass: String) : RegisterEvent()
    data class TermsAcceptedChanged(val accepted: Boolean) : RegisterEvent()
    data class RegisterWithGoogle(val idToken: String) : RegisterEvent()
    data class RegisterWithGoogleError(val message: String) : RegisterEvent()
    object Submit : RegisterEvent()
}

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged           -> _state.update { it.copy(name = event.name) }
            is RegisterEvent.EmailChanged          -> _state.update { it.copy(email = event.email) }
            is RegisterEvent.PassChanged           -> _state.update { it.copy(pass = event.pass) }
            is RegisterEvent.TermsAcceptedChanged  -> _state.update { it.copy(termsAccepted = event.accepted) }
            is RegisterEvent.RegisterWithGoogle    -> submitGoogleData(event.idToken)
            is RegisterEvent.RegisterWithGoogleError -> _state.update { it.copy(isLoading = false, errorMessage = event.message) }
            RegisterEvent.Submit                   -> submitData()
        }
    }

    private fun safeLaunch(block: suspend () -> Unit) = launchSafely(
        setLoading = { loading -> _state.update { it.copy(isLoading = loading) } },
        onError = { msg -> _state.update { it.copy(errorMessage = msg) } },
        block = block
    )

    private fun submitGoogleData(idToken: String) {
        safeLaunch {
            loginWithGoogleUseCase(idToken)
                .onSuccess { user -> _state.update { it.copy(success = true, user = user) } }
                .onFailure { e -> _state.update { it.copy(errorMessage = e.message ?: "Error al registrarse con Google") } }
        }
    }

    private fun submitData() {
        if (!_state.value.termsAccepted) {
            _state.update { it.copy(errorMessage = "Debes aceptar los términos y condiciones.") }
            return
        }
        val s = _state.value
        safeLaunch {
            registerUseCase(s.name, s.email, s.pass)
                .onSuccess { user -> _state.update { it.copy(success = true, user = user) } }
                .onFailure { e -> _state.update { it.copy(errorMessage = e.message ?: "Error desconocido") } }
        }
    }
}
