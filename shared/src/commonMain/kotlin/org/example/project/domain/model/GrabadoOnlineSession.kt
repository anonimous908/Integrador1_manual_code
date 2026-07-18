package org.example.project.domain.model

/**
 * Modelo de dominio que representa el estado de una sesión de grabación en vivo
 * (Grabado Online) para una receta o snippet de código.
 */
data class GrabadoOnlineSession(
    val isRecording: Boolean = false,
    val durationSeconds: Int = 0,
    val syncedSnapshots: Int = 0,
    val lastSyncMessage: String = "Listo para grabar"
)
