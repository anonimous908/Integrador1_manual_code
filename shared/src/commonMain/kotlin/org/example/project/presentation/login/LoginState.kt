package org.example.project.presentation.login

import org.example.project.domain.model.User

data class LoginState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passError: String? = null
)

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PassChanged(val pass: String) : LoginEvent
    object Submit : LoginEvent
    object Reset : LoginEvent
}
