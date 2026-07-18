package org.example.project.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class ThemeMode { LIGHT, DARK, DEFAULT }

val LocalAccentColor = compositionLocalOf { CodeNestColors.primary }
val LocalOnAccentColor = compositionLocalOf { CodeNestColors.onPrimary }
val LocalSetAccentColor = compositionLocalOf<(Color) -> Unit> { {} }
val LocalThemeMode = compositionLocalOf { ThemeMode.DARK }
val LocalSetThemeMode = compositionLocalOf<(ThemeMode) -> Unit> { {} }

val LocalSecondaryColor = compositionLocalOf { CodeNestColors.secondary }
val LocalOnSecondaryColor = compositionLocalOf { CodeNestColors.onSecondary }
val LocalTertiaryColor = compositionLocalOf { CodeNestColors.tertiary }
val LocalOnTertiaryColor = compositionLocalOf { CodeNestColors.onTertiary }
val LocalErrorColor = compositionLocalOf { CodeNestColors.error }
val LocalOnErrorColor = compositionLocalOf { CodeNestColors.onError }
val LocalSurfaceColor = compositionLocalOf { CodeNestColors.surface }

val CodeNestTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

fun onPrimaryForBackground(background: Color): Color {
    val luminance = 0.299 * background.red + 0.587 * background.green + 0.114 * background.blue
    return if (luminance > 0.5) Color.Black else Color.White
}

@Composable
fun CodeNestTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.DEFAULT -> true // DEFAULT uses original hardcoded dark colors
    }

    val scheme = if (isDark) {
        darkColorScheme(
            background = CodeNestColors.background,
            primary = CodeNestColors.primary,
            onPrimary = CodeNestColors.onPrimary,
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
            primary = CodeNestColors.primary,
            onPrimary = CodeNestColors.onPrimary,
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

    CompositionLocalProvider(
        LocalAccentColor provides CodeNestColors.primary,
        LocalOnAccentColor provides CodeNestColors.onPrimary,
        LocalSecondaryColor provides CodeNestColors.secondary,
        LocalOnSecondaryColor provides CodeNestColors.onSecondary,
        LocalTertiaryColor provides CodeNestColors.tertiary,
        LocalOnTertiaryColor provides CodeNestColors.onTertiary,
        LocalErrorColor provides CodeNestColors.error,
        LocalOnErrorColor provides CodeNestColors.onError,
        LocalSurfaceColor provides CodeNestColors.surface,
    ) {
        MaterialTheme(
            colorScheme = scheme,
            typography = CodeNestTypography,
            content = content
        )
    }
}
