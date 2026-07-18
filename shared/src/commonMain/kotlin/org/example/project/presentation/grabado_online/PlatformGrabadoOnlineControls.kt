package org.example.project.presentation.grabado_online

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformGrabadoOnlineButton(
    state: GrabadoOnlineState,
    onToggle: () -> Unit
)

@Composable
expect fun PlatformGrabadoOnlineBanner(
    state: GrabadoOnlineState,
    onOcrDetected: (String) -> Unit
)
