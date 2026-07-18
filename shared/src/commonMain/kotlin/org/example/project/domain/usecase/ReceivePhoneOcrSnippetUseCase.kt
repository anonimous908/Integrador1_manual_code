package org.example.project.domain.usecase

import org.example.project.domain.model.OcrCodeCapture

/**
 * Caso de uso (Clean Architecture) para procesar y limpiar texto de código recibido
 * desde la cámara/OCR de un dispositivo móvil antes de insertarlo en el editor de la laptop.
 */
class ReceivePhoneOcrSnippetUseCase {

    /**
     * Procesa y formatea el texto recibido de OCR para que tenga sintaxis clara.
     *
     * @param rawText Texto crudo extraído de la foto de la pizarra.
     * @param fileName Nombre sugerido de archivo.
     * @return [OcrCodeCapture] Captura lista para ser insertada en el editor.
     */
    operator fun invoke(rawText: String, fileName: String = "captura_pizarra.py"): OcrCodeCapture {
        val cleanedText = rawText.trim()
        return OcrCodeCapture(
            codeText = cleanedText,
            suggestedFileName = fileName,
            language = "Python",
            sourceDevice = "Celular -> OCR -> Laptop"
        )
    }
}
