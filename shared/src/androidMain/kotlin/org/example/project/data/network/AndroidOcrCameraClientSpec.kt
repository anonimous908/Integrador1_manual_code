package org.example.project.data.network

/**
 * Especificación e integración para la aplicación móvil Android que utiliza
 * Google ML Kit Text Recognition para capturar código de pizarras/pantallas.
 *
 * Flujo de transmisión real hacia CodeNest Desktop:
 * 1. La app Android procesa los fotogramas con `TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)`.
 * 2. Extrae bloques de texto e identifica indentación.
 * 3. Invoca [AndroidOcrClientBridge.onReceiveMlKitCapture] (o realiza POST HTTP al puerto local).
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
