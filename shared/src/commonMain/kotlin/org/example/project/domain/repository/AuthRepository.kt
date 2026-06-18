package org.example.project.domain.repository

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun register(name: String, email: String, pass: String): Result<Unit>
}
