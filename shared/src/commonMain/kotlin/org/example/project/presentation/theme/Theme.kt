package org.example.project.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode { LIGHT, DARK, SYSTEM }

val LocalAccentColor = compositionLocalOf { CodeNestColors.primary }
val LocalOnAccentColor = compositionLocalOf { CodeNestColors.onPrimary }
val LocalSetAccentColor = compositionLocalOf<(Color) -> Unit> { {} }
val LocalThemeMode = compositionLocalOf { ThemeMode.DARK }
val LocalSetThemeMode = compositionLocalOf<(ThemeMode) -> Unit> { {} }

fun onPrimaryForBackground(background: Color): Color {
    val luminance = 0.299 * background.red + 0.587 * background.green + 0.114 * background.blue
    return if (luminance > 0.5) Color.Black else Color.White
}

@Composable
fun CodeNestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: Color = CodeNestColors.primary,
    onAccentColor: Color = CodeNestColors.onPrimary,
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val scheme = if (isDark) {
        darkColorScheme(
            background = CodeNestColors.background,
            primary = accentColor,
            onPrimary = onAccentColor,
            primaryContainer = CodeNestColors.primaryContainer,
            onPrimaryContainer = CodeNestColors.onPrimaryContainer,
            surfaceContainerLow = CodeNestColors.surfaceContainerLow,
            surfaceContainer = CodeNestColors.surfaceContainer,
            surfaceContainerHigh = CodeNestColors.surfaceContainerHigh,
            onSurface = CodeNestColors.onSurface,
            onSurfaceVariant = CodeNestColors.onSurfaceVariant,
            outline = CodeNestColors.outline,
            outlineVariant = CodeNestColors.outlineVariant,
        )
    } else {
        lightColorScheme(
            background = CodeNestColors.background,
            primary = accentColor,
            onPrimary = onAccentColor,
            primaryContainer = CodeNestColors.primaryContainer,
            onPrimaryContainer = CodeNestColors.onPrimaryContainer,
            surfaceContainerLow = CodeNestColors.surfaceContainerLow,
            surfaceContainer = CodeNestColors.surfaceContainer,
            surfaceContainerHigh = CodeNestColors.surfaceContainerHigh,
            onSurface = CodeNestColors.onSurface,
            onSurfaceVariant = CodeNestColors.onSurfaceVariant,
            outline = CodeNestColors.outline,
            outlineVariant = CodeNestColors.outlineVariant,
        )
    }

    MaterialTheme(
        colorScheme = scheme,
        content = content
    )
}
