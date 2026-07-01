package org.example.project.data.network

import com.russhwolf.settings.Settings

data class AiConfig(
    val provider: String = "NVIDIA",
    val apiKey: String = "",
    val customEndpoint: String = "",
    val customModel: String = ""
)

class AiConfigRepository(private val settings: Settings) {

    fun loadConfig(): AiConfig {
        val provider = settings.getString("ai_provider", "NVIDIA")
        val apiKey = settings.getString("ai_api_key", "")
        val customEndpoint = settings.getString("ai_custom_endpoint", "")
        val customModel = settings.getString("ai_custom_model", "")
        return AiConfig(provider, apiKey, customEndpoint, customModel)
    }

    fun saveConfig(config: AiConfig) {
        settings.putString("ai_provider", config.provider)
        settings.putString("ai_api_key", config.apiKey)
        settings.putString("ai_custom_endpoint", config.customEndpoint)
        settings.putString("ai_custom_model", config.customModel)
    }

    fun getApiKey(): String = settings.getString("ai_api_key", "")

    fun getProvider(): String = settings.getString("ai_provider", "NVIDIA")

    fun getCustomEndpoint(): String = settings.getString("ai_custom_endpoint", "")

    fun getCustomModel(): String = settings.getString("ai_custom_model", "")
}
