package org.example.project.data.firebase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Interface for Firebase Auth operations.
 * Allows faking in tests.
 */
interface FirebaseAuthClientApi {
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun signUp(email: String, password: String, displayName: String? = null): Result<FirebaseUser>
}

/**
 * Ktor-based Firebase Auth REST client.
 *
 * Works on all KMP targets (Android, JVM, JS, WasmJS)
 * by calling the Firebase Auth REST API directly.
 */
class FirebaseAuthClient(
    private val httpClient: HttpClient
) : FirebaseAuthClientApi {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Sign in with email and password.
     * Returns a [FirebaseUser] on success.
     */
    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val response: SignInResponse = httpClient.post("${FirebaseConfig.authBaseUrl}/accounts:signInWithPassword") {
                parameter("key", FirebaseConfig.apiKey)
                contentType(ContentType.Application.Json)
                setBody(SignInRequest(email, password, true))
            }.body()

            Result.success(FirebaseUser(
                idToken = response.idToken,
                localId = response.localId,
                email = response.email,
                refreshToken = response.refreshToken,
                registered = response.registered,
                displayName = response.displayName
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create a new user with email and password.
     * Optionally accepts [displayName] to set the user's display name on registration.
     */
    override suspend fun signUp(email: String, password: String, displayName: String?): Result<FirebaseUser> {
        return try {
            val response: SignInResponse = httpClient.post("${FirebaseConfig.authBaseUrl}/accounts:signUp") {
                parameter("key", FirebaseConfig.apiKey)
                contentType(ContentType.Application.Json)
                setBody(SignInRequest(email, password, false, displayName))
            }.body()

            Result.success(FirebaseUser(
                idToken = response.idToken,
                localId = response.localId,
                email = response.email,
                refreshToken = response.refreshToken,
                registered = false,
                displayName = response.displayName
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class FirebaseUser(
    val idToken: String,
    val localId: String,
    val email: String,
    val refreshToken: String,
    val registered: Boolean,
    val displayName: String = ""
)

@Serializable
private data class SignInRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean,
    val displayName: String? = null
)

@Serializable
private data class SignInResponse(
    val idToken: String = "",
    val localId: String = "",
    val email: String = "",
    val refreshToken: String = "",
    val registered: Boolean = false,
    val displayName: String = ""
)
