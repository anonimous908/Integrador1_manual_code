package org.example.project.domain.usecase

import org.example.project.domain.model.User
import org.example.project.domain.repository.AuthRepository

/**
 * Caso de uso para autenticar a un usuario utilizando un token ID de Google.
 */
class LoginWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return authRepository.loginWithGoogle(idToken)
    }
}
