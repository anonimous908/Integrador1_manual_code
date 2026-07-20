package org.example.project.domain.repository

import org.example.project.domain.model.User

/**
 * Interfaz que define el contrato de operaciones de autenticación para CodeNest.
 */
interface AuthRepository {
    /**
     * Autentica a un usuario existente en el sistema.
     *
     * @param email [String] Correo electrónico del usuario.
     * @param pass [String] Contraseña del usuario.
     * @return [Result]<[User]> Retorna `Result.success([User])` si las credenciales son válidas,
     * o `Result.failure` con una excepción si son incorrectas o no existe el usuario.
     * @throws Exception Si ocurre un error de red o timeout durante el proceso.
     *
     * **Ejemplo de uso:**
     * ```kotlin
     * val result = authRepository.login("user@test.com", "Pass123!")
     * if (result.isSuccess) { navigateToHome() }
     * ```
     */
    suspend fun login(email: String, pass: String): Result<User>

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param name [String] Nombre completo del usuario.
     * @param email [String] Correo electrónico válido.
     * @param pass [String] Contraseña segura (mínimo 6 caracteres, mayúscula y número).
     * @return [Result]<[User]> Retorna `Result.success([User])` si el registro fue exitoso,
     * o `Result.failure` si el correo ya está registrado u ocurre un error.
     * @throws Exception Si ocurre un error de conectividad o de almacenamiento.
     *
     * **Ejemplo de uso:**
     * ```kotlin
     * val result = authRepository.register("Juan", "juan@test.com", "SecurePass1")
     * ```
     */
    suspend fun register(name: String, email: String, pass: String): Result<User>

    /**
     * Autentica al usuario en el sistema utilizando un token de Google OAuth / IdP.
     *
     * @param idToken [String] El id_token obtenido de Google Sign-In.
     * @return [Result]<[User]> Retorna el usuario autenticado si el token es válido.
     */
    suspend fun loginWithGoogle(idToken: String): Result<User>
}
