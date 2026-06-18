package org.example.project.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.data.network.GithubVersionChecker

class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    private val versionChecker = GithubVersionChecker()

    init {
        checkForUpdates()
    }

    private fun checkForUpdates() {
        viewModelScope.launch {
            val response = versionChecker.getLatestRelease()
            if (response != null) {
                // Remove the "v" prefix if present and compare
                val latestVersion = response.tag_name.removePrefix("v")
                val currentVersion = "1.1" // Hardcoded current version
                
                if (latestVersion > currentVersion) {
                    _state.update { it.copy(
                        updateAvailable = true,
                        latestVersionUrl = response.html_url
                    ) }
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginEvent.PassChanged -> {
                _state.update { it.copy(pass = event.pass) }
            }
            LoginEvent.Submit -> {
                submitData()
            }
            LoginEvent.Reset -> {
                _state.value = LoginState()
            }
        }
    }

    private fun submitData() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            loginWithEmailUseCase(_state.value.email, _state.value.pass)
                .onSuccess { _state.update { it.copy(isLoading = false, isLoggedIn = true) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error desconocido") } }
        }
    }
}
