package org.example.project.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
actual fun PlatformLoginActions(navigator: Navigator) {
    // Desktop (JVM): no se muestra el botón exclusivo de cámara Android
}
