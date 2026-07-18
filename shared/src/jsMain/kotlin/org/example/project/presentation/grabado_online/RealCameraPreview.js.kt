package org.example.project.presentation.grabado_online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
actual fun RealCameraPreview(
    modifier: Modifier,
    onOcrTextDetected: (String) -> Unit
) {
    Box(
        modifier = modifier.background(Color(0xFF0F0F0F)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Vista previa de cámara no disponible en Web",
            color = Color.LightGray,
            fontSize = 12.sp
        )
    }
}
