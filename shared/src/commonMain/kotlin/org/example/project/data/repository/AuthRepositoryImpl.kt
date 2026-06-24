package org.example.project.data.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.domain.repository.AuthRepository
import org.kotlincrypto.hash.sha2.SHA256

@Serializable
data class UserData(val name: String, val hashedPass: String)

class AuthRepositoryImpl(
    private val settings: Settings
) : AuthRepository {

    @OptIn(ExperimentalStdlibApi::class)
    private fun hashPassword(pass: String): String {
        return SHA256().digest(pass.encodeToByteArray()).toHexString()
    }

    override suspend fun register(name: String, email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.Default) {
            val storedData = settings.getStringOrNull("user_$email")
            if (storedData != null) {
                return@withContext Result.failure(Exception("El correo ya está registrado"))
            }

            val hashedPass = hashPassword(pass)
            val userData = UserData(name, hashedPass)
            
            settings.putString("user_$email", Json.encodeToString(userData))
            settings.putString("current_user", email)

            Result.success(Unit)
        }
    }

    override suspend fun login(email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.Default) {
            val storedData = settings.getStringOrNull("user_$email")
                ?: return@withContext Result.failure(Exception("Correo o contraseña incorrectos"))

            val userData = try {
                Json.decodeFromString<UserData>(storedData)
            } catch (e: Exception) {
                return@withContext Result.failure(Exception("La sesión expiró o los datos son incompatibles. Por favor, regístrate de nuevo."))
            }

            val inputHashedPass = hashPassword(pass)

            if (userData.hashedPass == inputHashedPass) {
                settings.putString("current_user", email)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Correo o contraseña incorrectos"))
            }
        }
    }
}
