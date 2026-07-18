package org.example.project.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

/**
 * Acciones adicionales de login especializadas por plataforma (por ejemplo, cámara OCR en vivo en Android).
 */
@Composable
expect fun PlatformLoginActions(navigator: Navigator)
