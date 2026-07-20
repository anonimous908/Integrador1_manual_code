package org.example.project.domain.usecase

import kotlinx.coroutines.test.runTest
import org.example.project.domain.model.User
import org.example.project.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegisterUseCaseTest {

    private val dummyRepository = object : AuthRepository {
        override suspend fun login(email: String, pass: String): Result<User> =
            Result.success(User(id = email, name = "Test", email = email))
        override suspend fun register(name: String, email: String, pass: String): Result<User> =
            Result.success(User(id = email, name = name, email = email))
        override suspend fun loginWithGoogle(idToken: String): Result<User> =
            Result.success(User(id = idToken, name = "Google User", email = "test@google.com"))
    }

    private val useCase = RegisterUseCase(dummyRepository)

    @Test
    fun testValidEmailFormat_ShouldSucceed() = runTest {
        val result = useCase("Test User", "test@example.com", "password123")
        assertTrue(result.isSuccess, "El registro debería ser exitoso con un correo válido.")
        val user = result.getOrNull()
        assertEquals("Test User", user?.name)
        assertEquals("test@example.com", user?.email)
    }

    @Test
    fun testInvalidEmailFormat_ShouldFail() = runTest {
        val result = useCase("Test User", "test-correo-invalido.com", "password123")
        assertTrue(result.isFailure, "El registro debería fallar con un correo inválido.")
        
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertTrue(exception.message?.contains("Formato de correo inválido") == true)
    }
    
    @Test
    fun testEmptyFields_ShouldFail() = runTest {
        val result = useCase("", "test@example.com", "123456")
        assertTrue(result.isFailure, "El registro debería fallar si hay campos vacíos.")
    }
}
