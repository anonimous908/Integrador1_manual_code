package org.example.project.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * Colores de la pantalla CodeNestLogin.
 * Extraídos acá para mantener el código de UI limpio
 * y poder reutilizarlos si después agregamos más pantallas.
 *
 * Basados en los colores del tailwind.config del proyecto original.
 */
object CodeNestColors {
    val background = Color(0xFF131313)
    val onBackground = Color(0xFFE5E2E1)
    val surface = Color(0xFF131313)
    val surfaceDim = Color(0xFF131313)
    val surfaceBright = Color(0xFF393939)
    val surfaceContainer = Color(0xFF201F1F)
    val surfaceContainerLow = Color(0xFF1C1B1B)
    val surfaceContainerLowest = Color(0xFF0E0E0E)
    val surfaceContainerHigh = Color(0xFF2A2A2A)
    val surfaceContainerHighest = Color(0xFF353534)
    val surfaceVariant = Color(0xFF353534)
    val onSurface = Color(0xFFE5E2E1)
    val onSurfaceVariant = Color(0xFFBEC7D4)
    val outline = Color(0xFF88919D)
    val outlineVariant = Color(0xFF3F4852)

    val primary = Color(0xFF98CBFF)
    val onPrimary = Color(0xFF003354)
    val primaryContainer = Color(0xFF00A3FF)
    val onPrimaryContainer = Color(0xFF00375A)
    val primaryFixed = Color(0xFFCFE5FF)

    val secondary = Color(0xFFD7FFC5)
    val onSecondary = Color(0xFF053900)
    val secondaryContainer = Color(0xFF2FF801)
    val onSecondaryContainer = Color(0xFF0F6D00)

    val tertiary = Color(0xFFD7BAFF)
    val onTertiary = Color(0xFF411478)
    val tertiaryContainer = Color(0xFFB289EE)
    val onTertiaryContainer = Color(0xFF451A7C)

    val error = Color(0xFFFFB4AB)
    val errorContainer = Color(0xFF93000A)

    // Colores del editor de código (estilo Dracula, igual al mockup)
    val codeBg = Color(0xFF1E1E1E)
    val codeKeyword = Color(0xFFFF79C6)
    val codeString = Color(0xFFF1FA8C)
    val codeType = Color(0xFF8BE9FD)
    val codeFunction = Color(0xFF50FA7B)
    val codeComment = Color(0xFF6272A4)
    val codeNumber = Color(0xFFBD93F9)
    val codeParam = Color(0xFFFFB86C)

    // Additions from Register
    val InputBackground   = Color(0xFF0A0A0A)
    val GlassPanel        = Color(0xFF252525).copy(alpha = 0.70f)
    val PrimaryGlow       = Color(0xFF98CBFF).copy(alpha = 0.05f)

    // Additions for Home and others
    val GoogleRed         = Color(0xFFEA4335)
    val GoogleYellow      = Color(0xFFFBBC05)
    val GoogleGreen       = Color(0xFF34A853)
    val GoogleBlue        = Color(0xFF4285F4)
}

/** Dimensiones del brand (logo + "Ztrene Studios") en la esquina inferior derecha. */
object CodeNestLoginBrand {
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
