package org.example.project.presentation.grabado_online

/**
 * Eventos de intención del usuario (Intents) para el módulo Grabado Online.
 */
sealed class GrabadoOnlineEvent {
    object ToggleRecording : GrabadoOnlineEvent()
    object ResetSession : GrabadoOnlineEvent()
    data class OnOcrCaptureReceived(
        val codeText: String,
        val sourceDevice: String = "Android ML Kit Camera"
    ) : GrabadoOnlineEvent()
}
