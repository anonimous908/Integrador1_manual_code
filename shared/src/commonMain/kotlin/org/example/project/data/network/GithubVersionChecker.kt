package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GithubReleaseResponse(
    val tag_name: String,
    val html_url: String
)

class GithubVersionChecker {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getLatestRelease(): GithubReleaseResponse? {
        return try {
            client.get("https://api.github.com/repos/anonimous908/Integrador1_manual_code/releases/latest").body()
        } catch (e: Exception) {
            println("VersionChecker Error: ${e.message}")
            null
        }
    }
}
