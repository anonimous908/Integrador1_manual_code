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
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
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

    /** Shared POST helper — eliminates repeated header/key boilerplate. */
    private suspend inline fun <reified B : Any> authPost(endpoint: String, body: B): Result<FirebaseUser> =
        runCatching {
            httpClient.post("${FirebaseConfig.authBaseUrl}/$endpoint") {
                parameter("key", FirebaseConfig.apiKey)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body<SignInResponse>().toFirebaseUser()
        }

    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> =
        authPost("accounts:signInWithPassword", SignInRequest(email, password, true))

    override suspend fun signUp(email: String, password: String, displayName: String?): Result<FirebaseUser> =
        authPost("accounts:signUp", SignInRequest(email, password, false, displayName))

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> =
        authPost("accounts:signInWithIdp", SignInWithIdpRequest(postBody = "id_token=$idToken&providerId=google.com"))
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
private data class SignInWithIdpRequest(
    val postBody: String,
    val requestUri: String = "http://localhost",
    val returnIdpCredential: Boolean = true,
    val returnSecureToken: Boolean = true
)

@Serializable
private data class SignInResponse(
    val idToken: String = "",
    val localId: String = "",
    val email: String = "",
    val refreshToken: String = "",
    val registered: Boolean = false,
    val displayName: String = ""
) {
    fun toFirebaseUser() = FirebaseUser(idToken, localId, email, refreshToken, registered, displayName)
}
