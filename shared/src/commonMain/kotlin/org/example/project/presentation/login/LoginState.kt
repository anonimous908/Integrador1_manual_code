package org.example.project.presentation.login

data class LoginState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val updateAvailable: Boolean = false,
    val latestVersionUrl: String? = null
)

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PassChanged(val pass: String) : LoginEvent
    object Submit : LoginEvent
    object Reset : LoginEvent
}
