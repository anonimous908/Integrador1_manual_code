package org.example.project.presentation.grabado_online

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformGrabadoOnlineButton(
    state: GrabadoOnlineState,
    onToggle: () -> Unit
) {
    GrabadoOnlineButton(state = state, onToggle = onToggle)
}

@Composable
actual fun PlatformGrabadoOnlineBanner(
    state: GrabadoOnlineState,
    onOcrDetected: (String) -> Unit
) {
    if (state.isRecording) {
        OcrSyncBanner(
            latestOcrCode = state.latestOcrCode,
            onReceiveOcrCapture = onOcrDetected
        )
    }
}
