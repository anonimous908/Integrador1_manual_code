package org.example.project.domain.usecase

import org.example.project.domain.model.GrabadoOnlineSession

/**
 * Caso de uso (Clean Architecture) para alternar el estado de grabación en vivo (Grabado Online).
 */
class ToggleGrabadoOnlineUseCase {

    /**
     * Alterna la sesión de grabación actual.
     *
     * @param currentSession Estado actual de la sesión.
     * @return [GrabadoOnlineSession] Nueva sesión actualizada.
     */
    operator fun invoke(currentSession: GrabadoOnlineSession): GrabadoOnlineSession {
        val newRecordingState = !currentSession.isRecording
        return if (newRecordingState) {
            currentSession.copy(
                isRecording = true,
                lastSyncMessage = "Grabando en vivo activado..."
            )
        } else {
            currentSession.copy(
                isRecording = false,
                lastSyncMessage = "Grabación en vivo pausada"
            )
        }
    }
}
