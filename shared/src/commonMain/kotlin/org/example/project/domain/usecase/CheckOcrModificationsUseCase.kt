package org.example.project.domain.usecase

/**
 * Resultado de la evaluación de modificaciones OCR respecto al archivo actual.
 */
data class OcrModificationResult(
    val hasModifications: Boolean,
    val cleanText: String,
    val reason: String
)

/**
 * Caso de uso (Clean Architecture) que evalúa si el texto extraído por OCR
 * contiene cambios respecto a la última versión conocida de `archivo.txt`.
 *
 * Evita llamadas redundantes y descarta capturas idénticas cada 10 segundos.
 */
class CheckOcrModificationsUseCase {

    operator fun invoke(newOcrText: String, lastKnownFileText: String): OcrModificationResult {
        val normalizedNew = newOcrText.trim()
        val normalizedOld = lastKnownFileText.trim()

        if (normalizedNew.isEmpty()) {
            return OcrModificationResult(
                hasModifications = false,
                cleanText = "",
                reason = "Captura vacía o sin texto detectado -> Ignorado."
            )
        }

        if (normalizedNew == normalizedOld) {
            return OcrModificationResult(
                hasModifications = false,
                cleanText = normalizedNew,
                reason = "Sin modificaciones respecto a archivo.txt -> Ignorado (Ahorro DeepSeek v4 Flash)."
            )
        }

        return OcrModificationResult(
            hasModifications = true,
            cleanText = normalizedNew,
            reason = "Diferencias detectadas -> Transmitiendo texto OCR actualizado."
        )
    }
}
