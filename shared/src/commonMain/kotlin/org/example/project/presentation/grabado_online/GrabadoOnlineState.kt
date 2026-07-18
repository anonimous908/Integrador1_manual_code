package org.example.project.presentation.grabado_online

import org.example.project.domain.model.GrabadoOnlineSession

/**
 * Estado inmutable para la UI del módulo Grabado Online (MVVM).
 */
data class GrabadoOnlineState(
    val session: GrabadoOnlineSession = GrabadoOnlineSession(),
    val isRecording: Boolean = false,
    val statusText: String = "Grabando en vivo",
    val latestOcrCode: String? = null,
    val ocrSourceDevice: String = "Android ML Kit Camera"
)
