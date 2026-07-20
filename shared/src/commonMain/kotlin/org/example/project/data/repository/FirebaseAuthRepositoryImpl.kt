package org.example.project.data.repository

import org.example.project.data.firebase.FirebaseAuthClientApi
import org.example.project.data.firebase.FirebaseUser
import org.example.project.domain.model.User
import org.example.project.domain.repository.AuthRepository

/**
 * Firebase-backed implementation of [AuthRepository].
 *
 * Delegates login and registration to [FirebaseAuthClientApi] (Ktor REST API),
 * mapping the Firebase domain model to the app's [User] domain model.
 * Works on all KMP targets (Android, JVM, JS, WasmJS).
 */
class FirebaseAuthRepositoryImpl(
    private val firebaseAuthClient: FirebaseAuthClientApi
) : AuthRepository {

    /** Maps a [FirebaseUser] response to a domain [User], using a fallback name when displayName is empty. */
    private fun FirebaseUser.toUser(nameFallback: String? = null) = User(
        id = localId,
        name = displayName.ifBlank { nameFallback ?: email },
        email = email
    )

    override suspend fun login(email: String, pass: String): Result<User> =
        firebaseAuthClient.signInWithEmail(email, pass).map { it.toUser() }

    override suspend fun register(name: String, email: String, pass: String): Result<User> =
        firebaseAuthClient.signUp(email, pass, displayName = name).map { it.toUser(nameFallback = name) }

    override suspend fun loginWithGoogle(idToken: String): Result<User> =
        firebaseAuthClient.signInWithGoogle(idToken).map { it.toUser() }
}
