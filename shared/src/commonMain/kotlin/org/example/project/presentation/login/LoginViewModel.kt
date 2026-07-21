package org.example.project.presentation.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.LoginWithGoogleUseCase
import org.example.project.domain.usecase.ValidateEmailUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.presentation.base.BaseViewModel

/**
 * ViewModel que gestiona el estado y la lógica de negocio de la pantalla de inicio de sesión.
 * Utiliza Casos de Uso (Clean Architecture) para validar datos y autenticar.
 */
class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, emailError = validateEmailUseCase(event.email), errorMessage = null) }
            }
            is LoginEvent.PassChanged -> {
                _state.update { it.copy(pass = event.pass, passError = validatePasswordUseCase(event.pass), errorMessage = null) }
            }
            is LoginEvent.LoginWithGoogle -> loginWithGoogle(event.idToken)
            LoginEvent.Submit -> submitData()
            LoginEvent.Reset -> _state.value = LoginState()
        }
    }

    private fun safeLaunch(block: suspend () -> Unit) = launchSafely(
        setLoading = { loading -> _state.update { it.copy(isLoading = loading) } },
        onError = { msg -> _state.update { it.copy(errorMessage = msg) } },
        block = block
    )

    private fun loginWithGoogle(idToken: String) {
        safeLaunch {
            loginWithGoogleUseCase(idToken)
                .onSuccess { user -> _state.update { it.copy(isLoggedIn = true, user = user) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.message ?: "Error al autenticar con Google") } }
        }
    }

    private fun submitData() {
        val s = _state.value
        val emailError = validateEmailUseCase(s.email)
        val passError = validatePasswordUseCase(s.pass)
        if (emailError != null || passError != null) {
            _state.update { it.copy(emailError = emailError, passError = passError, errorMessage = "Falta completar estos datos") }
            return
        }
        safeLaunch {
            loginWithEmailUseCase(s.email, s.pass)
                .onSuccess { user -> _state.update { it.copy(isLoggedIn = true, user = user) } }
                .onFailure { error -> _state.update { it.copy(errorMessage = error.message ?: "Error desconocido") } }
        }
    }
}
