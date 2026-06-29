package org.example.project.data.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.domain.repository.AuthRepository
import org.example.project.domain.service.HashService
import org.jetbrains.compose.resources.getString
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.error_email_registered
import kotlinproject.shared.generated.resources.error_invalid_credentials
import kotlinproject.shared.generated.resources.error_session_expired

@Serializable
data class UserData(val name: String, val hashedPass: String)

class AuthRepositoryImpl(
    private val settings: Settings,
    private val hashService: HashService
) : AuthRepository {

    override suspend fun register(name: String, email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.Default) {
            val storedData = settings.getStringOrNull("user_$email")
            if (storedData != null) {
                return@withContext Result.failure(Exception(getString(Res.string.error_email_registered)))
            }

            val hashedPass = hashService.hash(pass)
            val userData = UserData(name, hashedPass)
            
            settings.putString("user_$email", Json.encodeToString(userData))
            settings.putString("current_user", email)

            Result.success(Unit)
        }
    }

    override suspend fun login(email: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.Default) {
            val storedData = settings.getStringOrNull("user_$email")
                ?: return@withContext Result.failure(Exception(getString(Res.string.error_invalid_credentials)))

            val userData = try {
                Json.decodeFromString<UserData>(storedData)
            } catch (e: Exception) {
                return@withContext Result.failure(Exception(getString(Res.string.error_session_expired)))
            }

            val inputHashedPass = hashService.hash(pass)

            if (userData.hashedPass == inputHashedPass) {
                settings.putString("current_user", email)
                Result.success(Unit)
            } else {
                Result.failure(Exception(getString(Res.string.error_invalid_credentials)))
            }
        }
    }
}
