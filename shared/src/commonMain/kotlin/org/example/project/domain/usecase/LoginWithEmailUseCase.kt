package org.example.project.domain.usecase

import org.example.project.domain.repository.AuthRepository

class LoginWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        return authRepository.login(email, pass)
    }
}
