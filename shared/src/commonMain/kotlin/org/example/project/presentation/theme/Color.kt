package org.example.project.presentation.theme

import androidx.compose.runtime.mutableStateOf
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
    private val DarkSecondary = Color(0xFFD7FFC5)
    private val DarkOnSecondary = Color(0xFF053900)
    private val DarkTertiary = Color(0xFFD7BAFF)
    private val DarkOnTertiary = Color(0xFF411478)
    private val DarkError = Color(0xFFFFB4AB)
    private val DarkOnError = Color(0xFF690005)
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
    private val LightSecondary = Color(0xFFD7FFC5)
    private val LightOnSecondary = Color(0xFF053900)
    private val LightTertiary = Color(0xFFD7BAFF)
    private val LightOnTertiary = Color(0xFF411478)
    private val LightError = Color(0xFFFFB4AB)
    private val LightOnError = Color(0xFF690005)
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
    var secondary by mutableStateOf(DarkSecondary); private set
    var onSecondary by mutableStateOf(DarkOnSecondary); private set
    var tertiary by mutableStateOf(DarkTertiary); private set
    var onTertiary by mutableStateOf(DarkOnTertiary); private set
    var error by mutableStateOf(DarkError); private set
    var onError by mutableStateOf(DarkOnError); private set

    fun updateAccent(color: Color) {
        primary = color
        onPrimary = if (color.luminance() > 0.5) Color.Black else Color.White
    }

    fun updateSecondary(color: Color) {
        secondary = color
        onSecondary = if (color.luminance() > 0.5) Color.Black else Color.White
    }

    fun updateTertiary(color: Color) {
        tertiary = color
        onTertiary = if (color.luminance() > 0.5) Color.Black else Color.White
    }

    fun updateError(color: Color) {
        error = color
        onError = if (color.luminance() > 0.5) Color.Black else Color.White
    }

    fun updateSurface(color: Color) {
        surface = color
        surfaceDim = color
        surfaceBright = color
        surfaceContainer = color
        surfaceContainerLow = color
        surfaceContainerLowest = color
        surfaceContainerHigh = color
        surfaceContainerHighest = color
        surfaceVariant = color
    }

    fun resetToDefaults() {
        val isDark = surface == DarkSurface // heuristic: are we in dark mode?
        if (isDark) {
            primary = Color(0xFF98CBFF)
            onPrimary = Color(0xFF003354)
            secondary = DarkSecondary
            onSecondary = DarkOnSecondary
            tertiary = DarkTertiary
            onTertiary = DarkOnTertiary
            error = DarkError
            onError = DarkOnError
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
            primary = Color(0xFF98CBFF)
            onPrimary = Color(0xFF003354)
            secondary = LightSecondary
            onSecondary = LightOnSecondary
            tertiary = LightTertiary
            onTertiary = LightOnTertiary
            error = LightError
            onError = LightOnError
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

    fun updateTheme(isDark: Boolean) {
        val currentSecondary = secondary
        val currentTertiary = tertiary
        val currentError = error
        val currentSurface = surface // save customizations if any

        if (isDark) {
            background = DarkBackground
            onBackground = DarkOnBackground
            surface = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurface
            surfaceDim = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceDim
            surfaceBright = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceBright
            surfaceContainer = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceContainer
            surfaceContainerLow = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceContainerLow
            surfaceContainerLowest = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceContainerLowest
            surfaceContainerHigh = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceContainerHigh
            surfaceContainerHighest = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceContainerHighest
            surfaceVariant = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else DarkSurfaceVariant
            onSurface = DarkOnSurface
            onSurfaceVariant = DarkOnSurfaceVariant
            outline = DarkOutline
            outlineVariant = DarkOutlineVariant
            secondary = if (currentSecondary != LightSecondary && currentSecondary != DarkSecondary) currentSecondary else DarkSecondary
            onSecondary = if (currentSecondary == DarkSecondary || currentSecondary == LightSecondary) DarkOnSecondary else if (currentSecondary.luminance() > 0.5) Color.Black else Color.White
            tertiary = if (currentTertiary != LightTertiary && currentTertiary != DarkTertiary) currentTertiary else DarkTertiary
            onTertiary = if (currentTertiary == DarkTertiary || currentTertiary == LightTertiary) DarkOnTertiary else if (currentTertiary.luminance() > 0.5) Color.Black else Color.White
            error = if (currentError != LightError && currentError != DarkError) currentError else DarkError
            onError = if (currentError == DarkError || currentError == LightError) DarkOnError else if (currentError.luminance() > 0.5) Color.Black else Color.White
        } else {
            background = LightBackground
            onBackground = LightOnBackground
            surface = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurface
            surfaceDim = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceDim
            surfaceBright = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceBright
            surfaceContainer = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceContainer
            surfaceContainerLow = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceContainerLow
            surfaceContainerLowest = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceContainerLowest
            surfaceContainerHigh = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceContainerHigh
            surfaceContainerHighest = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceContainerHighest
            surfaceVariant = if (currentSurface != LightSurface && currentSurface != DarkSurface) currentSurface else LightSurfaceVariant
            onSurface = LightOnSurface
            onSurfaceVariant = LightOnSurfaceVariant
            outline = LightOutline
            outlineVariant = LightOutlineVariant
            secondary = if (currentSecondary != LightSecondary && currentSecondary != DarkSecondary) currentSecondary else LightSecondary
            onSecondary = if (currentSecondary == DarkSecondary || currentSecondary == LightSecondary) LightOnSecondary else if (currentSecondary.luminance() > 0.5) Color.Black else Color.White
            tertiary = if (currentTertiary != LightTertiary && currentTertiary != DarkTertiary) currentTertiary else LightTertiary
            onTertiary = if (currentTertiary == DarkTertiary || currentTertiary == LightTertiary) LightOnTertiary else if (currentTertiary.luminance() > 0.5) Color.Black else Color.White
            error = if (currentError != LightError && currentError != DarkError) currentError else LightError
            onError = if (currentError == DarkError || currentError == LightError) LightOnError else if (currentError.luminance() > 0.5) Color.Black else Color.White
        }
    }

    private fun Color.luminance(): Double = 0.299 * red + 0.587 * green + 0.114 * blue

    val primaryContainer = Color(0xFF00A3FF)
    val onPrimaryContainer = Color(0xFF00375A)
    val primaryFixed = Color(0xFFCFE5FF)

    val secondaryContainer = Color(0xFF2FF801)
    val onSecondaryContainer = Color(0xFF0F6D00)

    val tertiaryContainer = Color(0xFFB289EE)
    val onTertiaryContainer = Color(0xFF451A7C)

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
