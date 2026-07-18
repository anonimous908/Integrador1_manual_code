package org.example.project.presentation.grabado_online

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable multi-plataforma para mostrar el visor de cámara REAL (hardware).
 * En Android utiliza CameraX PreviewView + Google ML Kit Text Recognition en tiempo real.
 */
@Composable
expect fun RealCameraPreview(
    modifier: Modifier = Modifier,
    onOcrTextDetected: (String) -> Unit
)
