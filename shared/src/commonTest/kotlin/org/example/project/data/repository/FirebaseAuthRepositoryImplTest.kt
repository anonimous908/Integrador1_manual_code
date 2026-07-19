package org.example.project.data.repository

import kotlinx.coroutines.test.runTest
import org.example.project.data.firebase.FirebaseAuthClientApi
import org.example.project.data.firebase.FirebaseUser
import org.example.project.domain.model.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for [FirebaseAuthRepositoryImpl].
 *
 * Uses a fake [FirebaseAuthClientApi] to avoid real network calls.
 */
class FirebaseAuthRepositoryImplTest {

    private val fakeClient = FakeFirebaseAuthClient()
    private val repository = FirebaseAuthRepositoryImpl(firebaseAuthClient = fakeClient)

    @Test
    fun `login maps FirebaseUser to domain User correctly`() = runTest {
        fakeClient.signInResult = Result.success(
            FirebaseUser(
                idToken = "token-1",
                localId = "firebase-uid-123",
                email = "user@example.com",
                refreshToken = "refresh-token-1",
                registered = true,
                displayName = "John Doe"
            )
        )

        val result = repository.login("user@example.com", "password123")
        assertTrue(result.isSuccess, "login should succeed")

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("firebase-uid-123", user.id)
        assertEquals("John Doe", user.name)
        assertEquals("user@example.com", user.email)
    }

    @Test
    fun `login uses email as fallback name when displayName is empty`() = runTest {
        fakeClient.signInResult = Result.success(
            FirebaseUser(
                idToken = "token-2",
                localId = "firebase-uid-456",
                email = "anon@example.com",
                refreshToken = "refresh-token-2",
                registered = true,
                displayName = ""
            )
        )

        val result = repository.login("anon@example.com", "password123")
        assertTrue(result.isSuccess)

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("anon@example.com", user.name, "should fall back to email when displayName is blank")
    }

    @Test
    fun `login propagates failure when FirebaseAuthClient fails`() = runTest {
        fakeClient.signInResult = Result.failure(Exception("INVALID_PASSWORD"))

        val result = repository.login("user@example.com", "wrong-password")
        assertTrue(result.isFailure, "login should fail")
        assertEquals("INVALID_PASSWORD", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register maps FirebaseUser to domain User correctly`() = runTest {
        fakeClient.signUpResult = Result.success(
            FirebaseUser(
                idToken = "token-3",
                localId = "firebase-uid-789",
                email = "newuser@example.com",
                refreshToken = "refresh-token-3",
                registered = false,
                displayName = "Jane Smith"
            )
        )

        val result = repository.register("Jane Smith", "newuser@example.com", "SecurePass1!")
        assertTrue(result.isSuccess, "register should succeed")

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("firebase-uid-789", user.id)
        assertEquals("Jane Smith", user.name)
        assertEquals("newuser@example.com", user.email)
    }

    @Test
    fun `register uses provided name when displayName is empty in response`() = runTest {
        fakeClient.signUpResult = Result.success(
            FirebaseUser(
                idToken = "token-4",
                localId = "firebase-uid-012",
                email = "nouser@example.com",
                refreshToken = "refresh-token-4",
                registered = false,
                displayName = ""
            )
        )

        val result = repository.register("Provided Name", "nouser@example.com", "SecurePass1!")
        assertTrue(result.isSuccess)

        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("Provided Name", user.name, "should use the name passed to register()")
    }

    @Test
    fun `register propagates failure when FirebaseAuthClient fails`() = runTest {
        fakeClient.signUpResult = Result.failure(Exception("EMAIL_EXISTS"))

        val result = repository.register("Test User", "existing@example.com", "SecurePass1!")
        assertTrue(result.isFailure, "register should fail")
        assertEquals("EMAIL_EXISTS", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login passes email to FirebaseAuthClient`() = runTest {
        fakeClient.signInResult = Result.success(
            FirebaseUser(
                idToken = "token-5",
                localId = "uid-5",
                email = "capture@example.com",
                refreshToken = "rt-5",
                registered = true,
                displayName = "Capture"
            )
        )

        repository.login("capture@example.com", "mypass")
        assertEquals("capture@example.com", fakeClient.lastSignInEmail)
        assertEquals("mypass", fakeClient.lastSignInPassword)
    }

    @Test
    fun `register passes name email and password to FirebaseAuthClient`() = runTest {
        fakeClient.signUpResult = Result.success(
            FirebaseUser(
                idToken = "token-6",
                localId = "uid-6",
                email = "reg@example.com",
                refreshToken = "rt-6",
                registered = false,
                displayName = "Register User"
            )
        )

        repository.register("Register User", "reg@example.com", "StrongPass1!")
        assertEquals("reg@example.com", fakeClient.lastSignUpEmail)
        assertEquals("StrongPass1!", fakeClient.lastSignUpPassword)
        assertEquals("Register User", fakeClient.lastSignUpDisplayName)
    }
}

/**
 * A fake [FirebaseAuthClientApi] that allows controlling signIn/signUp results in tests.
 */
class FakeFirebaseAuthClient : FirebaseAuthClientApi {
    var signInResult: Result<FirebaseUser> = Result.success(
        FirebaseUser("", "", "", "", false, "")
    )
    var signUpResult: Result<FirebaseUser> = Result.success(
        FirebaseUser("", "", "", "", false, "")
    )

    var lastSignInEmail: String? = null
    var lastSignInPassword: String? = null
    var lastSignUpEmail: String? = null
    var lastSignUpPassword: String? = null
    var lastSignUpDisplayName: String? = null

    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        lastSignInEmail = email
        lastSignInPassword = password
        return signInResult
    }

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return signInResult
    }

    override suspend fun signUp(email: String, password: String, displayName: String?): Result<FirebaseUser> {
        lastSignUpEmail = email
        lastSignUpPassword = password
        lastSignUpDisplayName = displayName
        return signUpResult
    }
}
