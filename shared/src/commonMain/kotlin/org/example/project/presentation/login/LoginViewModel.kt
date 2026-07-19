package org.example.project.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.LoginWithGoogleUseCase
import org.example.project.domain.usecase.ValidateEmailUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase

/**
 * ViewModel que gestiona el estado y la lógica de negocio de la pantalla de inicio de sesión.
 * Utiliza Casos de Uso (Clean Architecture) para validar datos y autenticar.
 */
class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    /**
     * Procesa los eventos de intención (Intents) enviados desde la UI.
     *
     * @param event [LoginEvent] El evento específico desencadenado por el usuario (e.g. EmailChanged, Submit).
     * @return No retorna ningún valor. Muta internamente el estado [state].
     * @throws Exception No lanza excepciones directas, los errores de autenticación se manejan en el estado.
     *
     * **Ejemplo de uso:**
     * ```kotlin
     * viewModel.onEvent(LoginEvent.EmailChanged("nuevo@correo.com"))
     * viewModel.onEvent(LoginEvent.Submit)
     * ```
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                val error = validateEmailUseCase(event.email)
                _state.update { it.copy(email = event.email, emailError = error, errorMessage = null) }
            }
            is LoginEvent.PassChanged -> {
                val error = validatePasswordUseCase(event.pass)
                _state.update { it.copy(pass = event.pass, passError = error, errorMessage = null) }
            }
            is LoginEvent.LoginWithGoogle -> {
                loginWithGoogle(event.idToken)
            }
            LoginEvent.Submit -> {
                submitData()
            }
            LoginEvent.Reset -> {
                _state.value = LoginState()
            }
        }
    }

    private fun loginWithGoogle(idToken: String) {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            loginWithGoogleUseCase(idToken)
                .onSuccess { user -> _state.update { it.copy(isLoading = false, isLoggedIn = true, user = user) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error al autenticar con Google") } }
        }
    }

    private fun submitData() {
        val currentState = _state.value
        val emailError = validateEmailUseCase(currentState.email)
        val passError = validatePasswordUseCase(currentState.pass)

        if (emailError != null || passError != null) {
            _state.update { 
                it.copy(
                    emailError = emailError, 
                    passError = passError,
                    errorMessage = "Falta completar estos datos"
                ) 
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            loginWithEmailUseCase(currentState.email, currentState.pass)
                .onSuccess { user -> _state.update { it.copy(isLoading = false, isLoggedIn = true, user = user) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error desconocido") } }
        }
    }
}
