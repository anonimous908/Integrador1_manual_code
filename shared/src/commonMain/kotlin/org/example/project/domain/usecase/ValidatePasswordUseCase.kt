package org.example.project.domain.usecase

class ValidatePasswordUseCase {
    private val upperRegex = ".*[A-Z].*".toRegex()
    private val digitRegex = ".*[0-9].*".toRegex()

    operator fun invoke(password: String): String? = when {
        password.isBlank() -> "La contraseña no puede estar vacía"
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        !password.matches(upperRegex) -> "La contraseña debe contener al menos una mayúscula"
        !password.matches(digitRegex) -> "La contraseña debe contener al menos un número"
        else -> null
    }
}

