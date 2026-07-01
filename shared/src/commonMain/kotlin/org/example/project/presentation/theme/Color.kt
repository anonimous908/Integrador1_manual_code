package org.example.project.presentation.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    // ─── Paleta oscura (default) ──────────────────────────────────────────
    private val DarkBackground = Color(0xFF131313)
    private val DarkOnBackground = Color(0xFFE5E2E1)
    private val DarkSurface = Color(0xFF131313)
    private val DarkSurfaceDim = Color(0xFF131313)
    private val DarkSurfaceBright = Color(0xFF393939)
    private val DarkSurfaceContainer = Color(0xFF201F1F)
    private val DarkSurfaceContainerLow = Color(0xFF1C1B1B)
    private val DarkSurfaceContainerLowest = Color(0xFF0E0E0E)
    private val DarkSurfaceContainerHigh = Color(0xFF2A2A2A)
    private val DarkSurfaceContainerHighest = Color(0xFF353534)
    private val DarkSurfaceVariant = Color(0xFF353534)
    private val DarkOnSurface = Color(0xFFE5E2E1)
    private val DarkOnSurfaceVariant = Color(0xFFBEC7D4)
    private val DarkOutline = Color(0xFF88919D)
    private val DarkOutlineVariant = Color(0xFF3F4852)

    // ─── Paleta clara ─────────────────────────────────────────────────────
    private val LightBackground = Color(0xFFF8F9FA)
    private val LightOnBackground = Color(0xFF1A1A2E)
    private val LightSurface = Color(0xFFFFFFFF)
    private val LightSurfaceDim = Color(0xFFE8E8ED)
    private val LightSurfaceBright = Color(0xFFFFFFFF)
    private val LightSurfaceContainer = Color(0xFFF0F0F5)
    private val LightSurfaceContainerLow = Color(0xFFF5F5FA)
    private val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
    private val LightSurfaceContainerHigh = Color(0xFFE5E5EA)
    private val LightSurfaceContainerHighest = Color(0xFFD9D9E0)
    private val LightSurfaceVariant = Color(0xFFE0E0E5)
    private val LightOnSurface = Color(0xFF1A1A2E)
    private val LightOnSurfaceVariant = Color(0xFF5F6368)
    private val LightOutline = Color(0xFFDADCE0)
    private val LightOutlineVariant = Color(0xFFE8EAED)

    // ─── Valores reactivos (seleccionan oscuro o claro según el tema) ────
    var background by mutableStateOf(DarkBackground); private set
    var onBackground by mutableStateOf(DarkOnBackground); private set
    var surface by mutableStateOf(DarkSurface); private set
    var surfaceDim by mutableStateOf(DarkSurfaceDim); private set
    var surfaceBright by mutableStateOf(DarkSurfaceBright); private set
    var surfaceContainer by mutableStateOf(DarkSurfaceContainer); private set
    var surfaceContainerLow by mutableStateOf(DarkSurfaceContainerLow); private set
    var surfaceContainerLowest by mutableStateOf(DarkSurfaceContainerLowest); private set
    var surfaceContainerHigh by mutableStateOf(DarkSurfaceContainerHigh); private set
    var surfaceContainerHighest by mutableStateOf(DarkSurfaceContainerHighest); private set
    var surfaceVariant by mutableStateOf(DarkSurfaceVariant); private set
    var onSurface by mutableStateOf(DarkOnSurface); private set
    var onSurfaceVariant by mutableStateOf(DarkOnSurfaceVariant); private set
    var outline by mutableStateOf(DarkOutline); private set
    var outlineVariant by mutableStateOf(DarkOutlineVariant); private set

    var primary by mutableStateOf(Color(0xFF98CBFF)); private set
    var onPrimary by mutableStateOf(Color(0xFF003354)); private set

    fun updateAccent(color: Color) {
        primary = color
        val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
        onPrimary = if (luminance > 0.5) Color.Black else Color.White
    }

    fun updateTheme(isDark: Boolean) {
        if (isDark) {
            background = DarkBackground
            onBackground = DarkOnBackground
            surface = DarkSurface
            surfaceDim = DarkSurfaceDim
            surfaceBright = DarkSurfaceBright
            surfaceContainer = DarkSurfaceContainer
            surfaceContainerLow = DarkSurfaceContainerLow
            surfaceContainerLowest = DarkSurfaceContainerLowest
            surfaceContainerHigh = DarkSurfaceContainerHigh
            surfaceContainerHighest = DarkSurfaceContainerHighest
            surfaceVariant = DarkSurfaceVariant
            onSurface = DarkOnSurface
            onSurfaceVariant = DarkOnSurfaceVariant
            outline = DarkOutline
            outlineVariant = DarkOutlineVariant
        } else {
            background = LightBackground
            onBackground = LightOnBackground
            surface = LightSurface
            surfaceDim = LightSurfaceDim
            surfaceBright = LightSurfaceBright
            surfaceContainer = LightSurfaceContainer
            surfaceContainerLow = LightSurfaceContainerLow
            surfaceContainerLowest = LightSurfaceContainerLowest
            surfaceContainerHigh = LightSurfaceContainerHigh
            surfaceContainerHighest = LightSurfaceContainerHighest
            surfaceVariant = LightSurfaceVariant
            onSurface = LightOnSurface
            onSurfaceVariant = LightOnSurfaceVariant
            outline = LightOutline
            outlineVariant = LightOutlineVariant
        }
    }
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
