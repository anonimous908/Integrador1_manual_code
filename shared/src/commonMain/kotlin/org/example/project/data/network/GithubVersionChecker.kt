package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import io.github.aakira.napier.Napier

@Serializable
data class GithubReleaseResponse(
    val tag_name: String,
    val html_url: String
)

class GithubVersionChecker(
    private val client: HttpClient
) {
    suspend fun getLatestRelease(): GithubReleaseResponse? {
        return try {
            client.get("https://api.github.com/repos/anonimous908/Integrador1_manual_code/releases/latest").body()
        } catch (e: Exception) {
            Napier.e(e) { "VersionChecker Error: ${e.message}" }
            null
        }
    }
}
