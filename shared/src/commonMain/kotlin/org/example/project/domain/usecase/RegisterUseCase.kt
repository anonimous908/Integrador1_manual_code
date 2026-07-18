package org.example.project.domain.usecase

import org.example.project.domain.model.User
import org.example.project.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, pass: String): Result<User> {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos son requeridos."))
        }
        if (!EmailValidator.isValid(email)) {
            return Result.failure(IllegalArgumentException("Formato de correo inválido."))
        }
        if (pass.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 6 caracteres."))
        }
        return authRepository.register(name, email, pass)
    }
}
