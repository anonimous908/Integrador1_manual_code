package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.github.aakira.napier.Napier

private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

@Serializable
data class ChatMessage(val role: String, val content: String)

@Serializable
data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val max_tokens: Int = 4096,
    val temperature: Double = 0.3
)

@Serializable
data class ChatCompletionChoice(
    val message: ChatMessage,
    val finish_reason: String? = null
)

@Serializable
data class ChatUsage(
    val prompt_tokens: Int = 0,
    val completion_tokens: Int = 0,
    val total_tokens: Int = 0
)

@Serializable
data class ChatCompletionResponse(
    val choices: List<ChatCompletionChoice> = emptyList(),
    val usage: ChatUsage? = null
)

data class TranscriptionResult(val language: String, val code: String)

enum class AiProvider(val displayName: String, val baseUrl: String, val defaultModel: String) {
    NVIDIA("NVIDIA", "https://integrate.api.nvidia.com/v1", "nvidia/llama-3.1-nemotron-70b-instruct"),
    DEEPSEEK("DeepSeek", "https://api.deepseek.com/v1", "deepseek-chat"),
    CLAUDE("Claude", "https://api.anthropic.com/v1", "claude-3-haiku-20240307"),
    CHATGPT("ChatGPT", "https://api.openai.com/v1", "gpt-4o-mini"),
    CUSTOM("Personalizado", "", "");

    companion object {
        fun fromDisplayName(name: String): AiProvider =
            entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: CUSTOM
    }
}

class AiApiService(
    private val client: HttpClient,
    private val configRepo: AiConfigRepository
) {
    suspend fun analyzeCode(
        code: String,
        language: String,
        instruction: String = "Analiza el siguiente código y explica qué hace, posibles errores, y sugiere mejoras."
    ): Result<String> {
        val config = configRepo.loadConfig()
        if (config.apiKey.isBlank()) {
            return Result.failure(Exception("No se ha configurado una API key. Ve a Configuración > Sincronización."))
        }

        val provider = AiProvider.fromDisplayName(config.provider)
        val baseUrl = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomEndpoint()
        } else {
            provider.baseUrl
        }

        if (baseUrl.isBlank()) {
            return Result.failure(Exception("URL del proveedor no configurada para '${config.provider}'."))
        }

        val model = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomModel()
        } else {
            provider.defaultModel
        }

        val prompt = buildString {
            appendLine("Eres un asistente experto en programación. Analiza el siguiente código en **$language**.")
            appendLine()
            appendLine("Instrucción: $instruction")
            appendLine()
            appendLine("```$language")
            appendLine(code)
            appendLine("```")
        }

        return sendChatRequest(baseUrl, model, config.apiKey, prompt, provider)
    }

    suspend fun chat(
        userMessage: String,
        systemPrompt: String = "Eres un asistente de programación experto. Responde con precisión técnica."
    ): Result<String> {
        val config = configRepo.loadConfig()
        if (config.apiKey.isBlank()) {
            return Result.failure(Exception("No se ha configurado una API key."))
        }

        val provider = AiProvider.fromDisplayName(config.provider)
        val baseUrl = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomEndpoint()
        } else {
            provider.baseUrl
        }
        val model = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomModel()
        } else {
            provider.defaultModel
        }

        val messages = listOf(
            ChatMessage("system", systemPrompt),
            ChatMessage("user", userMessage)
        )

        return sendChatWithMessages(baseUrl, model, config.apiKey, messages, provider)
    }

    private suspend fun sendChatRequest(
        baseUrl: String,
        model: String,
        apiKey: String,
        prompt: String,
        provider: AiProvider
    ): Result<String> {
        val messages = listOf(ChatMessage("user", prompt))
        return sendChatWithMessages(baseUrl, model, apiKey, messages, provider)
    }

    private suspend fun sendChatWithMessages(
        baseUrl: String,
        model: String,
        apiKey: String,
        messages: List<ChatMessage>,
        provider: AiProvider
    ): Result<String> {
        return try {
            val request = ChatCompletionRequest(
                model = model,
                messages = messages,
                max_tokens = 4096,
                temperature = 0.3
            )

            val response: ChatCompletionResponse = client.post("${baseUrl}/chat/completions") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $apiKey")
                if (provider == AiProvider.CLAUDE) {
                    header("anthropic-version", "2023-06-01")
                    header("x-api-key", apiKey)
                }
                setBody(json.encodeToString(ChatCompletionRequest.serializer(), request))
            }.body()

            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("El modelo devolvió una respuesta vacía."))
            } else {
                Result.success(content)
            }
        } catch (e: Exception) {
            Napier.e(e) { "AiApiService Error: ${e.message}" }
            Result.failure(Exception("Error al comunicarse con ${provider.displayName}: ${e.message}"))
        }
    }

    fun getAvailableLanguages(): List<String> = listOf(
        "Kotlin", "Java", "Python", "JavaScript", "TypeScript", "C", "C++", "C#",
        "Rust", "Go", "Swift", "Ruby", "PHP", "Dart", "Scala", "R",
        "Assembly (x86)", "Assembly (ARM)", "Shell/Bash", "PowerShell",
        "SQL", "HTML", "CSS", "YAML", "Dockerfile", "Makefile", "CMake",
        "LLVM IR", "WebAssembly (WAT)", "VHDL", "Verilog", "MATLAB", "Fortran",
        "COBOL", "Lua", "Perl", "Haskell", "Elixir", "Erlang", "Clojure",
        "Groovy", "Objective-C", "Zig", "Nim", "Julia"
    )

    suspend fun transcribeCodeFromImage(
        imageBytes: ByteArray,
        instruction: String
    ): Result<TranscriptionResult> {
        val config = configRepo.loadConfig()
        if (config.apiKey.isBlank()) {
            return Result.failure(Exception("No se ha configurado una API key."))
        }

        val provider = AiProvider.fromDisplayName(config.provider)
        val baseUrl = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomEndpoint()
        } else {
            provider.baseUrl
        }

        if (baseUrl.isBlank()) {
            return Result.failure(Exception("URL del proveedor no configurada."))
        }

        val model = if (provider == AiProvider.CUSTOM) {
            configRepo.getCustomModel()
        } else {
            when (provider) {
                AiProvider.CHATGPT -> "gpt-4o"
                AiProvider.DEEPSEEK -> "deepseek-chat"
                AiProvider.CLAUDE -> "claude-3-haiku-20240307"
                AiProvider.NVIDIA -> "nvidia/llama-3.1-nemotron-70b-instruct"
                else -> provider.defaultModel
            }
        }

        val base64Image = encodeBase64(imageBytes)
        val mimeType = detectMimeType(imageBytes)

        val prompt = buildString {
            appendLine("Eres un OCR y detector de lenguajes de programación.")
            appendLine()
            appendLine("Paso 1: Mira la imagen y detecta qué lenguaje de programación contiene.")
            appendLine("Paso 2: Transcribe EXACTAMENTE el código visible.")
            appendLine()
            appendLine("Instrucción del usuario: ${instruction.ifBlank { "Transcribe el código de esta imagen" }}")
            appendLine()
            appendLine("Responde ÚNICAMENTE en este formato exacto (sin markdown adicional):")
            appendLine()
            appendLine("LANG: [nombre del lenguaje]")
            appendLine("---")
            appendLine("[código transcrito]")
            appendLine()
            appendLine("Si no hay código en la imagen, responde: LANG: NONE")
        }

        return sendVisionRequest(baseUrl, model, config.apiKey, base64Image, mimeType, prompt, provider)
            .map { raw ->
                val lang = extractLangFromResponse(raw)
                val code = extractCodeFromResponse(raw)
                TranscriptionResult(lang, code)
            }
    }

    private fun extractLangFromResponse(raw: String): String {
        val match = Regex("^LANG:\\s*(.+?)$", RegexOption.MULTILINE).find(raw)
        return match?.groupValues?.get(1)?.trim() ?: ""
    }

    private fun extractCodeFromResponse(raw: String): String {
        val separatorIndex = raw.indexOf("---")
        return if (separatorIndex >= 0) {
            raw.substring(separatorIndex + 3).trim()
        } else {
            raw.replaceFirst(Regex("^LANG:\\s*.+?\\n", RegexOption.MULTILINE), "").trim()
        }
    }

    private suspend fun sendVisionRequest(
        baseUrl: String,
        model: String,
        apiKey: String,
        base64Image: String,
        mimeType: String,
        prompt: String,
        provider: AiProvider
    ): Result<String> {
        return try {
            val requestBody = buildString {
                append("""{"model":"$model","messages":[{"role":"user","content":[""")
                append("""{"type":"text","text":${buildJsonString(prompt)}},""")
                append("""{"type":"image_url","image_url":{"url":"data:$mimeType;base64,$base64Image"}}""")
                append(""")}],"max_tokens":4096,"temperature":0.2}""")
            }

            val responseText: String = client.post("${baseUrl}/chat/completions") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $apiKey")
                if (provider == AiProvider.CLAUDE) {
                    header("anthropic-version", "2023-06-01")
                    header("x-api-key", apiKey)
                }
                setBody(requestBody)
            }.body()

            val response = json.decodeFromString(ChatCompletionResponse.serializer(), responseText)
            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("El modelo devolvió una respuesta vacía."))
            } else {
                Result.success(content)
            }
        } catch (e: Exception) {
            Napier.e(e) { "Vision API Error: ${e.message}" }
            Result.failure(Exception("Error al analizar la imagen: ${e.message}"))
        }
    }

    private fun buildJsonString(text: String): String {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\""
    }

    private fun detectMimeType(bytes: ByteArray): String {
        if (bytes.size < 4) return "image/png"
        return when {
            bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() -> "image/jpeg"
            bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() -> "image/png"
            bytes[0] == 0x47.toByte() && bytes[1] == 0x49.toByte() -> "image/gif"
            bytes[0] == 0x42.toByte() && bytes[1] == 0x4D.toByte() -> "image/bmp"
            bytes[0] == 0x52.toByte() && bytes[1] == 0x49.toByte() -> "image/webp"
            else -> "image/png"
        }
    }
}

private val BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

private fun encodeBase64(bytes: ByteArray): String {
    val result = StringBuilder()
    var i = 0
    while (i < bytes.size) {
        val b0 = bytes[i].toInt() and 0xFF
        val b1 = if (i + 1 < bytes.size) bytes[i + 1].toInt() and 0xFF else 0
        val b2 = if (i + 2 < bytes.size) bytes[i + 2].toInt() and 0xFF else 0
        result.append(BASE64_ALPHABET[b0 shr 2])
        result.append(BASE64_ALPHABET[((b0 and 0x03) shl 4) or (b1 shr 4)])
        result.append(if (i + 1 < bytes.size) BASE64_ALPHABET[((b1 and 0x0F) shl 2) or (b2 shr 6)] else '=')
        result.append(if (i + 2 < bytes.size) BASE64_ALPHABET[b2 and 0x3F] else '=')
        i += 3
    }
    return result.toString()
}
