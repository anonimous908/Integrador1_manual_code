package org.example.project.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * Colores de la pantalla DevSpaceLogin.
 * Extraídos acá para mantener el código de UI limpio
 * y poder reutilizarlos si después agregamos más pantallas.
 *
 * Basados en los colores del tailwind.config del proyecto original.
 */
object DevSpaceLoginColors {
    val background = Color(0xFF131313)
    val primary = Color(0xFF98cbff)
    val primaryContainer = Color(0xFF00a3ff)
    val onPrimaryContainer = Color(0xFF00375a)
    val surfaceContainer = Color(0xFF201f1f)
    val surfaceContainerHigh = Color(0xFF2a2a2a)
    val surfaceContainerLow = Color(0xFF1c1b1b)
    val onSurface = Color(0xFFe5e2e1)
    val onSurfaceVariant = Color(0xFFbec7d4)
    val outline = Color(0xFF88919d)
    val outlineVariant = Color(0xFF3f4852)
}

/** Dimensiones del brand (logo + "Ztrene Studios") en la esquina inferior derecha. */
object DevSpaceLoginBrand {
    /** Tamaño base del logo en dp. Se multiplica por scaleFactor. */
    val logoSize: Dp = 56.dp

    /** Tamaño mínimo del logo (escala 0.5x). */
    val logoMin: Dp = 28.dp

    /** Tamaño máximo del logo (escala 1.5x). */
    val logoMax: Dp = 84.dp

    /** Separación vertical entre logo y texto. */
    val logoTextSpacer: Dp = 2.dp

    /** Tamaño base del texto "Ztrene Studios" en sp. Se multiplica por scaleFactor. */
    const val textSize: Float = 12f

    /** Tamaño mínimo del texto. */
    const val textMin: Float = 9f

    /** Tamaño máximo del texto. */
    const val textMax: Float = 16f
}
