package org.example.project.data.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.project.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val settings: Settings
) : AuthRepository {

    override suspend fun register(name: String, email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlinx.coroutines.delay(800)

            val storedData = settings.getStringOrNull("user_$email")
            if (storedData != null) {
                return@withContext Result.failure(Exception("El correo ya está registrado"))
            }

            val hashedPass = pass.hashCode().toString()
            
            settings.putString("user_$email", "$name|$hashedPass")
            settings.putString("current_user", email)

            Result.success(Unit)
        }
    }

    override suspend fun login(email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlinx.coroutines.delay(800)

            val storedData = settings.getStringOrNull("user_$email")
                ?: return@withContext Result.failure(Exception("Correo o contraseña incorrectos"))

            val parts = storedData.split("|")
            if (parts.size != 2) return@withContext Result.failure(Exception("Datos corruptos"))

            val storedHashedPass = parts[1]
            val inputHashedPass = pass.hashCode().toString()

            if (storedHashedPass == inputHashedPass) {
                settings.putString("current_user", email)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Correo o contraseña incorrectos"))
            }
        }
    }
}
