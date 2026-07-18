package org.example.project.domain.model

/**
 * Modelo de dominio que representa una captura de código obtenida por la cámara del celular
 * convertida a texto puro (OCR) para su consumo por un LLM (ej. DeepSeek) o inserción directa
 * en el editor de la laptop al tener "Grabando en vivo" activo.
 */
data class OcrCodeCapture(
    val codeText: String,
    val suggestedFileName: String = "captura_pizarra.py",
    val language: String = "Python",
    val sourceDevice: String = "Cámara Celular (OCR)"
)
