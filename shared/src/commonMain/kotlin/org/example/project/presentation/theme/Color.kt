package org.example.project.presentation.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CodeNestPalette(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val surfaceDim: Color,
    val surfaceBright: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val surfaceContainer: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color
)

val DARK_PALETTE = CodeNestPalette(
    background = Color(0xFF131313),
    onBackground = Color(0xFFE5E2E1),
    surface = Color(0xFF131313),
    surfaceDim = Color(0xFF131313),
    surfaceBright = Color(0xFF393939),
    onSurface = Color(0xFFE5E2E1),
    onSurfaceVariant = Color(0xFFBEC7D4),
    surfaceContainer = Color(0xFF201F1F),
    surfaceContainerLow = Color(0xFF1C1B1B),
    surfaceContainerLowest = Color(0xFF0E0E0E),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF353534),
    surfaceVariant = Color(0xFF353534),
    primary = Color(0xFF98CBFF),
    onPrimary = Color(0xFF003354),
    primaryContainer = Color(0xFF00A3FF),
    onPrimaryContainer = Color(0xFF00375A),
    secondary = Color(0xFFD7FFC5),
    onSecondary = Color(0xFF053900),
    secondaryContainer = Color(0xFF2FF801),
    onSecondaryContainer = Color(0xFF0F6D00),
    tertiary = Color(0xFFD7BAFF),
    onTertiary = Color(0xFF411478),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF88919D),
    outlineVariant = Color(0xFF3F4852),
    inverseSurface = Color(0xFFE8E8ED),
    inverseOnSurface = Color(0xFF1A1A2E)
)

val LIGHT_PALETTE = CodeNestPalette(
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1A2E),
    surface = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFE8E8ED),
    surfaceBright = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A2E),
    onSurfaceVariant = Color(0xFF5F6368),
    surfaceContainer = Color(0xFFF0F0F5),
    surfaceContainerLow = Color(0xFFF5F5FA),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFE5E5EA),
    surfaceContainerHighest = Color(0xFFD9D9E0),
    surfaceVariant = Color(0xFFE0E0E5),
    primary = Color(0xFF98CBFF),
    onPrimary = Color(0xFF003354),
    primaryContainer = Color(0xFF00A3FF),
    onPrimaryContainer = Color(0xFF00375A),
    secondary = Color(0xFFD7FFC5),
    onSecondary = Color(0xFF053900),
    secondaryContainer = Color(0xFF2FF801),
    onSecondaryContainer = Color(0xFF0F6D00),
    tertiary = Color(0xFFD7BAFF),
    onTertiary = Color(0xFF411478),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFFDADCE0),
    outlineVariant = Color(0xFFE8EAED),
    inverseSurface = Color(0xFF393939),
    inverseOnSurface = Color(0xFFE5E2E1)
)

object CodeNestColors {
    private var _isDark = true
    private var _palette by mutableStateOf(DARK_PALETTE)

    val palette: CodeNestPalette get() = _palette

    val background: Color get() = _palette.background
    val onBackground: Color get() = _palette.onBackground
    val surface: Color get() = _palette.surface
    val surfaceDim: Color get() = _palette.surfaceDim
    val surfaceBright: Color get() = _palette.surfaceBright
    val onSurface: Color get() = _palette.onSurface
    val onSurfaceVariant: Color get() = _palette.onSurfaceVariant
    val surfaceContainer: Color get() = _palette.surfaceContainer
    val surfaceContainerLow: Color get() = _palette.surfaceContainerLow
    val surfaceContainerLowest: Color get() = _palette.surfaceContainerLowest
    val surfaceContainerHigh: Color get() = _palette.surfaceContainerHigh
    val surfaceContainerHighest: Color get() = _palette.surfaceContainerHighest
    val surfaceVariant: Color get() = _palette.surfaceVariant
    val primary: Color get() = _palette.primary
    val onPrimary: Color get() = _palette.onPrimary
    val primaryContainer: Color get() = _palette.primaryContainer
    val onPrimaryContainer: Color get() = _palette.onPrimaryContainer
    val secondary: Color get() = _palette.secondary
    val onSecondary: Color get() = _palette.onSecondary
    val secondaryContainer: Color get() = _palette.secondaryContainer
    val onSecondaryContainer: Color get() = _palette.onSecondaryContainer
    val tertiary: Color get() = _palette.tertiary
    val onTertiary: Color get() = _palette.onTertiary
    val error: Color get() = _palette.error
    val onError: Color get() = _palette.onError
    val errorContainer: Color get() = _palette.errorContainer
    val onErrorContainer: Color get() = _palette.onErrorContainer
    val outline: Color get() = _palette.outline
    val outlineVariant: Color get() = _palette.outlineVariant
    val inverseSurface: Color get() = _palette.inverseSurface
    val inverseOnSurface: Color get() = _palette.inverseOnSurface

    // Static colors (not theme-dependent)
    val primaryFixed = Color(0xFFCFE5FF)
    val tertiaryContainer = Color(0xFFB289EE)
    val onTertiaryContainer = Color(0xFF451A7C)
    val codeBg = Color(0xFF1E1E1E)
    val codeKeyword = Color(0xFFFF79C6)
    val codeString = Color(0xFFF1FA8C)
    val codeType = Color(0xFF8BE9FD)
    val codeFunction = Color(0xFF50FA7B)
    val codeComment = Color(0xFF6272A4)
    val codeNumber = Color(0xFFBD93F9)
    val codeParam = Color(0xFFFFB86C)
    val InputBackground = Color(0xFF0A0A0A)
    val GlassPanel = Color(0xFF252525).copy(alpha = 0.70f)
    val PrimaryGlow = Color(0xFF98CBFF).copy(alpha = 0.05f)
    val GoogleRed = Color(0xFFEA4335)
    val GoogleYellow = Color(0xFFFBBC05)
    val GoogleGreen = Color(0xFF34A853)
    val GoogleBlue = Color(0xFF4285F4)

    fun updateAccent(color: Color) {
        _palette = _palette.copy(
            primary = color,
            onPrimary = if (color.luminance() > 0.5) Color.Black else Color.White
        )
    }

    fun updateSecondary(color: Color) {
        _palette = _palette.copy(
            secondary = color,
            onSecondary = if (color.luminance() > 0.5) Color.Black else Color.White
        )
    }

    fun updateTertiary(color: Color) {
        _palette = _palette.copy(
            tertiary = color,
            onTertiary = if (color.luminance() > 0.5) Color.Black else Color.White
        )
    }

    fun updateError(color: Color) {
        _palette = _palette.copy(
            error = color,
            onError = if (color.luminance() > 0.5) Color.Black else Color.White
        )
    }

    fun updateSurface(color: Color) {
        _palette = _palette.copy(
            surface = color,
            surfaceDim = color,
            surfaceBright = color,
            surfaceContainer = color,
            surfaceContainerLow = color,
            surfaceContainerLowest = color,
            surfaceContainerHigh = color,
            surfaceContainerHighest = color,
            surfaceVariant = color
        )
    }

    fun resetToDefaults() {
        _palette = if (_isDark) DARK_PALETTE else LIGHT_PALETTE
    }

    fun updateTheme(isDark: Boolean) {
        _isDark = isDark
        _palette = if (isDark) DARK_PALETTE else LIGHT_PALETTE
    }

}

internal fun Color.luminance(): Double = 0.299 * red + 0.587 * green + 0.114 * blue

object CodeNestLoginBrand {
    val logoSize: Dp = 56.dp
    val logoMin: Dp = 28.dp
    val logoMax: Dp = 84.dp
    val logoTextSpacer: Dp = 2.dp
    const val textSize: Float = 12f
    const val textMin: Float = 9f
    const val textMax: Float = 16f
}
