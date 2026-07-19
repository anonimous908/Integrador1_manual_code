package org.example.project.presentation.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.project.domain.model.User
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.usecase.LoginWithEmailUseCase
import org.example.project.domain.usecase.ValidateEmailUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── Task 3.2: LoginState defaults ───────────────────────────────────

    @Test
    fun `loginState defaults are correct`() {
        val state = LoginState()

        assertEquals("", state.email, "email should default to empty string")
        assertEquals("", state.pass, "pass should default to empty string")
        assertEquals(false, state.isLoading, "isLoading should default to false")
        assertEquals(false, state.isLoggedIn, "isLoggedIn should default to false")
        assertNull(state.user, "user should default to null")
        assertNull(state.errorMessage, "errorMessage should default to null")
    }

    // ─── Task 3.1: LoginViewModel behavior ──────────────────────────────

    @Test
    fun `emailChanged updates state email`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("algo@mail.com"))

        assertEquals("algo@mail.com", viewModel.state.value.email)
    }

    @Test
    fun `emailChanged with different email updates state correctly`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("otro@dominio.com"))

        assertEquals("otro@dominio.com", viewModel.state.value.email)
    }

    @Test
    fun `passChanged updates state pass`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.PassChanged("123"))

        assertEquals("123", viewModel.state.value.pass)
    }

    @Test
    fun `passChanged with different value updates state correctly`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.PassChanged("passwordSegura99"))

        assertEquals("passwordSegura99", viewModel.state.value.pass)
    }

    @Test
    fun `login success sets isLoggedIn to true, stores user, and clears loading`() = runTest {
        val testUser = User(id = "test-id", name = "Test User", email = "test@example.com")
        val fakeRepo = FakeAuthRepository().apply {
            loginResult = Result.success(testUser)
        }
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PassChanged("Password123"))
        viewModel.onEvent(LoginEvent.Submit)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isLoggedIn, "isLoggedIn should be true after successful login")
        assertEquals(false, viewModel.state.value.isLoading, "isLoading should be false after completion")
        assertNull(viewModel.state.value.errorMessage, "errorMessage should be null after successful login")
        assertNotNull(viewModel.state.value.user, "user should not be null after successful login")
        assertEquals("test-id", viewModel.state.value.user?.id)
        assertEquals("Test User", viewModel.state.value.user?.name)
        assertEquals("test@example.com", viewModel.state.value.user?.email)
    }

    @Test
    fun `login failure sets errorMessage and does NOT set isLoggedIn`() = runTest {
        val fakeRepo = FakeAuthRepository().apply {
            loginResult = Result.failure(Exception("Credenciales inválidas"))
        }
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PassChanged("Password123"))
        viewModel.onEvent(LoginEvent.Submit)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Credenciales inválidas", viewModel.state.value.errorMessage)
        assertEquals(false, viewModel.state.value.isLoggedIn, "isLoggedIn should remain false on failure")
        assertEquals(false, viewModel.state.value.isLoading, "isLoading should be false after completion")
    }

    @Test
    fun `login failure with different error message`() = runTest {
        val fakeRepo = FakeAuthRepository().apply {
            loginResult = Result.failure(Exception("Usuario no encontrado"))
        }
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PassChanged("Password123"))
        viewModel.onEvent(LoginEvent.Submit)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Usuario no encontrado", viewModel.state.value.errorMessage)
    }

    @Test
    fun `reset returns state to initial values`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        viewModel.onEvent(LoginEvent.EmailChanged("algo@mail.com"))
        viewModel.onEvent(LoginEvent.PassChanged("123"))
        viewModel.onEvent(LoginEvent.Reset)

        assertEquals("", viewModel.state.value.email, "email should be reset to empty")
        assertEquals("", viewModel.state.value.pass, "pass should be reset to empty")
        assertNull(viewModel.state.value.errorMessage, "errorMessage should be reset to null")
        assertEquals(false, viewModel.state.value.isLoading, "isLoading should be reset to false")
    }

    @Test
    fun `reset after event sequence clears previous state`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        val viewModel = LoginViewModel(useCase, ValidateEmailUseCase(), ValidatePasswordUseCase())

        // Accumulate state changes
        viewModel.onEvent(LoginEvent.EmailChanged("user@test.com"))
        viewModel.onEvent(LoginEvent.PassChanged("mypassword"))
        // Now reset
        viewModel.onEvent(LoginEvent.Reset)

        // All fields should be back to defaults
        assertEquals("", viewModel.state.value.email)
        assertEquals("", viewModel.state.value.pass)
        assertEquals(false, viewModel.state.value.isLoggedIn)
        assertEquals(false, viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.errorMessage)
    }

    @Test
    fun `when LoginWithGoogle event is processed, user is logged in successfully`() = runTest {
        viewModel.onEvent(LoginEvent.LoginWithGoogle("fake-google-id-token"))

        assertTrue(viewModel.state.value.isLoggedIn)
        assertEquals("test@example.com", viewModel.state.value.user?.email)
        assertNull(viewModel.state.value.errorMessage)
    }
}

/**
 * A fake [AuthRepository] that allows controlling login/register results in tests.
 */
class FakeAuthRepository : AuthRepository {
    var loginResult: Result<User> = Result.success(User(id = "test-id", name = "Test User", email = "test@example.com"))
    var registerResult: Result<User> = Result.success(User(id = "test-id", name = "Test User", email = "test@example.com"))
    var googleLoginResult: Result<User> = Result.success(User(id = "test-id", name = "Test User", email = "test@example.com"))

    override suspend fun login(email: String, pass: String): Result<User> = loginResult
    override suspend fun register(name: String, email: String, pass: String): Result<User> = registerResult
    override suspend fun loginWithGoogle(idToken: String): Result<User> = googleLoginResult
}
