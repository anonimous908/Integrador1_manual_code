package org.example.project.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = CodeNestColors.background,
    primary = CodeNestColors.primary,
    primaryContainer = CodeNestColors.primaryContainer,
    onPrimaryContainer = CodeNestColors.onPrimaryContainer,
    surfaceContainer = CodeNestColors.surfaceContainer,
    surfaceContainerHigh = CodeNestColors.surfaceContainerHigh,
    surfaceContainerLow = CodeNestColors.surfaceContainerLow,
    onSurface = CodeNestColors.onSurface,
    onSurfaceVariant = CodeNestColors.onSurfaceVariant,
    outline = CodeNestColors.outline,
    outlineVariant = CodeNestColors.outlineVariant,
)

private val LightColorScheme = lightColorScheme(
    background = CodeNestColors.background,
    primary = CodeNestColors.primary,
    primaryContainer = CodeNestColors.primaryContainer,
    onPrimaryContainer = CodeNestColors.onPrimaryContainer,
    surfaceContainer = CodeNestColors.surfaceContainer,
    surfaceContainerHigh = CodeNestColors.surfaceContainerHigh,
    surfaceContainerLow = CodeNestColors.surfaceContainerLow,
    onSurface = CodeNestColors.onSurface,
    onSurfaceVariant = CodeNestColors.onSurfaceVariant,
    outline = CodeNestColors.outline,
    outlineVariant = CodeNestColors.outlineVariant,
)

@Composable
fun CodeNestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
