package org.example.project.domain.usecase

import org.example.project.domain.repository.AuthRepository

class LoginWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        if (!EmailValidator.isValid(email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        return authRepository.login(email, pass)
    }
}
