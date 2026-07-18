package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Cliente para DeepSeek v4 Flash para procesar texto OCR puro (sin imágenes).
 */
class DeepSeekV4FlashClient {

    companion object {
        const val API_KEY = "sk-4fb849f2db0449b5ada56b9821c58a27"
        const val MODEL_NAME = "deepseek-chat"
        const val ENDPOINT_URL = "https://api.deepseek.com/chat/completions"
    }

    private val httpClient = HttpClient()

    /**
     * Envía el texto OCR extraído a DeepSeek v4 Flash para su verificación o análisis.
     * Recuerda: DeepSeek v4 Flash solo acepta texto puro, no imágenes.
     */
    suspend fun analyzeOcrText(ocrText: String): String {
        if (ocrText.isBlank()) return "Texto OCR vacío -> Ignorado por DeepSeek v4 Flash."
        return try {
            val requestBody = """
                {
                  "model": "$MODEL_NAME",
                  "messages": [
                    {"role": "system", "content": "Eres un asistente de análisis de código OCR. Verifica y limpia el código extraído de la pizarra."},
                    {"role": "user", "content": "Analiza este texto OCR:\\n${ocrText.replace("\"", "\\\"").replace("\n", "\\n")}"}
                  ],
                  "stream": false
                }
            """.trimIndent()

            val response: HttpResponse = httpClient.post(ENDPOINT_URL) {
                header("Authorization", "Bearer $API_KEY")
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            if (response.status.isSuccess()) {
                "✅ DeepSeek v4 Flash verificado correctamente (API Key activa)."
            } else {
                "DeepSeek v4 Flash Response Status: ${response.status.value}"
            }
        } catch (e: Exception) {
            "DeepSeek v4 Flash Error: ${e.message ?: "Conexión en curso"}"
        }
    }
}
