package org.example.project.data.network

import org.example.project.domain.service.LiveOcrReceiverHub
import org.example.project.domain.usecase.ReceivePhoneOcrSnippetUseCase

/**
 * Adaptador de infraestructura que gestiona la recepción de capturas entrantes desde
 * la aplicación móvil Android con Google ML Kit Text Recognition.
 */
class AndroidOcrClientBridge(
    private val receivePhoneOcrSnippetUseCase: ReceivePhoneOcrSnippetUseCase = ReceivePhoneOcrSnippetUseCase()
) {
    /**
     * Procesa y emite una nueva captura entrante proveniente del módulo de Android ML Kit.
     *
     * @param rawOcrText Texto detectado en la foto de la pizarra/pantalla por ML Kit.
     * @param deviceName Identificador del celular Android emisor.
     * @return true si la emisión fue procesada exitosamente.
     */
    fun onReceiveMlKitCapture(rawOcrText: String, deviceName: String = "Android ML Kit Camera"): Boolean {
        val ocrCapture = receivePhoneOcrSnippetUseCase(rawOcrText).copy(sourceDevice = deviceName)
        return LiveOcrReceiverHub.tryEmitOcrCapture(ocrCapture)
    }
}

/**
 * Especificación e integración para la aplicación móvil Android que utiliza
 * Google ML Kit Text Recognition para capturar código de pizarras/pantallas.
 */
object AndroidOcrCameraClientSpec {
    const val DEFAULT_EMBEDDED_PORT = 8088
    const val ENDPOINT_PATH = "/api/live-ocr"

    fun formatPayloadJson(ocrText: String, deviceName: String = "Android ML Kit"): String {
        return """
        {
          "device": "$deviceName",
          "ocr_text": "${ocrText.replace("\"", "\\\"").replace("\n", "\\n")}",
          "timestamp": ${System.currentTimeMillis()}
        }
        """.trimIndent()
    }
}
