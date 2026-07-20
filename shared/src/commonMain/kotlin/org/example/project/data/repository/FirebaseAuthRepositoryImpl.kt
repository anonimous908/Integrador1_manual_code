package org.example.project.data.repository

import org.example.project.data.firebase.FirebaseAuthClientApi
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

    override suspend fun login(email: String, pass: String): Result<User> {
        return firebaseAuthClient.signInWithEmail(email, pass)
            .map { firebaseUser ->
                User(
                    id = firebaseUser.localId,
                    name = firebaseUser.displayName.ifBlank { firebaseUser.email },
                    email = firebaseUser.email
                )
            }
    }

    override suspend fun register(name: String, email: String, pass: String): Result<User> {
        return firebaseAuthClient.signUp(email, pass, displayName = name)
            .map { firebaseUser ->
                User(
                    id = firebaseUser.localId,
                    name = firebaseUser.displayName.ifBlank { name },
                    email = firebaseUser.email
                )
            }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return firebaseAuthClient.signInWithGoogle(idToken)
            .map { firebaseUser ->
                User(
                    id = firebaseUser.localId,
                    name = firebaseUser.displayName.ifBlank { firebaseUser.email },
                    email = firebaseUser.email
                )
            }
    }
}
