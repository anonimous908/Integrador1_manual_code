package org.example.project.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import org.example.project.domain.model.OcrCodeCapture
import org.example.project.domain.service.LiveOcrReceiverHub

/**
 * Canal de sincronización remota entre dispositivos (Celular Android -> PC Desktop).
 * Utiliza un canal ligero HTTP para que la PC reciba en tiempo real las capturas de la cámara móvil.
 */
object RemoteOcrSyncRelay {
    private const val RELAY_URL = "https://ntfy.sh/codenest-live-ocr-sync-borge908"
    private val httpClient = HttpClient()
    private var lastReceivedMessageId: String = ""
    private var isListening = false

    /**
     * Envía el texto OCR capturado por el celular Android hacia el canal remoto.
     */
    suspend fun sendOcrCapture(ocrText: String) {
        if (ocrText.isBlank()) return
        try {
            httpClient.post(RELAY_URL) {
                setBody(ocrText)
            }
        } catch (_: Exception) {
            // Error de red ignorado para no bloquear UI
        }
    }

    /**
     * Inicia la escucha activa en Desktop para recibir capturas del celular.
     */
    fun startListeningRemoteCaptures(scope: CoroutineScope) {
        if (isListening) return
        isListening = true
        scope.launch(Dispatchers.Default) {
            while (isActive) {
                try {
                    val responseText = httpClient.get("$RELAY_URL/json?poll=1").bodyAsText()
                    // Procesar líneas JSON entrantes
                    responseText.lines().forEach { line ->
                        if (line.contains("\"message\":")) {
                            val msgIndex = line.indexOf("\"message\":\"")
                            if (msgIndex != -1) {
                                val start = msgIndex + 11
                                val end = line.indexOf("\"", start)
                                if (end != -1) {
                                    val unescapedText = line.substring(start, end)
                                        .replace("\\n", "\n")
                                        .replace("\\\"", "\"")
                                        .replace("\\\\", "\\")
                                    if (unescapedText.isNotBlank() && unescapedText != lastReceivedMessageId) {
                                        lastReceivedMessageId = unescapedText
                                        LiveOcrReceiverHub.emitOcrCapture(
                                            OcrCodeCapture(
                                                codeText = unescapedText,
                                                sourceDevice = "Celular Android (Moto g75 5G)"
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } catch (_: Exception) {
                    // Esperar en caso de fallo temporal
                }
                delay(2500)
            }
        }
    }
}
