package org.example.project.domain.usecase

private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
fun isValidEmail(email: String): Boolean = email.matches(EMAIL_REGEX)

class ValidateEmailUseCase {
    operator fun invoke(email: String): String? = when {
        email.isBlank() -> "El correo no puede estar vacío"
        !isValidEmail(email) -> "Formato de correo inválido"
        else -> null
    }
}

