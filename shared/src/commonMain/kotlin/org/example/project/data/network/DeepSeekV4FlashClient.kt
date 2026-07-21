package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.*

/**
 * Cliente para DeepSeek v4 Flash para procesar texto OCR puro con soporte de flujo progresivo y optimización de latencia.
 */
class DeepSeekV4FlashClient {

    companion object {
        const val API_KEY = "sk-4fb849f2db0449b5ada56b9821c58a27"
        const val MODEL_NAME = "deepseek-chat"
        const val ENDPOINT_URL = "https://api.deepseek.com/chat/completions"
        private const val SYSTEM_PROMPT = "Eres un experto programador en tecnologías modernas (Kotlin, Python, Compose). Corrige y limpia el código OCR extraído devolviendo ÚNICAMENTE el código verificado y listo sin rodeos ni verbosidad innecesaria."
    }

    private val httpClient = HttpClient()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    /** Builds the shared DeepSeek JSON request body. */
    private fun buildRequestBody(ocrText: String): String = buildJsonObject {
        put("model", MODEL_NAME)
        put("stream", false)
        put("temperature", 0.2)
        put("max_tokens", 800)
        put("messages", buildJsonArray {
            add(buildJsonObject { put("role", "system"); put("content", SYSTEM_PROMPT) })
            add(buildJsonObject { put("role", "user"); put("content", "Analiza y limpia este texto OCR:\n$ocrText") })
        })
    }.toString()

    /** Extracts content text from a DeepSeek JSON response string. */
    private fun extractContent(responseText: String): String? = try {
        jsonParser.parseToJsonElement(responseText).jsonObject["choices"]
            ?.jsonArray?.firstOrNull()?.jsonObject
            ?.get("message")?.jsonObject
            ?.get("content")?.jsonPrimitive?.contentOrNull
    } catch (_: Exception) { responseText }

    /**
     * Consume DeepSeek emitiendo la salida de forma reactiva y cancelable en tiempo real.
     */
    fun analyzeOcrTextStream(ocrText: String): Flow<String> = flow {
        if (ocrText.isBlank()) { emit("Texto OCR vacío -> Ignorado por DeepSeek v4 Flash."); return@flow }

        emit("⌛ Conectando con DeepSeek v4 Flash...")
        val response = httpClient.post(ENDPOINT_URL) {
            header("Authorization", "Bearer $API_KEY")
            contentType(ContentType.Application.Json)
            setBody(buildRequestBody(ocrText))
        }
        if (!response.status.isSuccess()) { emit("❌ Error en API DeepSeek Status: ${response.status.value}"); return@flow }

        emit(extractContent(response.bodyAsText()) ?: "✅ DeepSeek v4 Flash verificado.")
    }.catch { e ->
        if (e !is kotlin.coroutines.cancellation.CancellationException &&
            e !is kotlinx.coroutines.CancellationException) {
            emit("❌ Error en análisis: ${e.message ?: "Conexión interrumpida"}")
        }
    }.flowOn(Dispatchers.Default)

    /**
     * Envía el texto OCR extraído a DeepSeek en modo rápido síncrono.
     */
    suspend fun analyzeOcrText(ocrText: String): String {
        if (ocrText.isBlank()) return "Texto OCR vacío -> Ignorado por DeepSeek v4 Flash."
        return try {
            val response = httpClient.post(ENDPOINT_URL) {
                header("Authorization", "Bearer $API_KEY")
                contentType(ContentType.Application.Json)
                setBody(buildRequestBody(ocrText))
            }
            if (response.status.isSuccess()) {
                extractContent(response.bodyAsText()) ?: "✅ DeepSeek v4 Flash verificado."
            } else {
                "DeepSeek v4 Flash Response Status: ${response.status.value}"
            }
        } catch (e: Exception) {
            "DeepSeek v4 Flash Error: ${e.message ?: "Conexión en curso"}"
        }
    }
}
