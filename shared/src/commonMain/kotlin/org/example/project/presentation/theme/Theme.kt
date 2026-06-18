package org.example.project.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = DevSpaceColors.background,
    primary = DevSpaceColors.primary,
    primaryContainer = DevSpaceColors.primaryContainer,
    onPrimaryContainer = DevSpaceColors.onPrimaryContainer,
    surfaceContainer = DevSpaceColors.surfaceContainer,
    surfaceContainerHigh = DevSpaceColors.surfaceContainerHigh,
    surfaceContainerLow = DevSpaceColors.surfaceContainerLow,
    onSurface = DevSpaceColors.onSurface,
    onSurfaceVariant = DevSpaceColors.onSurfaceVariant,
    outline = DevSpaceColors.outline,
    outlineVariant = DevSpaceColors.outlineVariant,
)

private val LightColorScheme = lightColorScheme(
    background = DevSpaceColors.background,
    primary = DevSpaceColors.primary,
    primaryContainer = DevSpaceColors.primaryContainer,
    onPrimaryContainer = DevSpaceColors.onPrimaryContainer,
    surfaceContainer = DevSpaceColors.surfaceContainer,
    surfaceContainerHigh = DevSpaceColors.surfaceContainerHigh,
    surfaceContainerLow = DevSpaceColors.surfaceContainerLow,
    onSurface = DevSpaceColors.onSurface,
    onSurfaceVariant = DevSpaceColors.onSurfaceVariant,
    outline = DevSpaceColors.outline,
    outlineVariant = DevSpaceColors.outlineVariant,
)

@Composable
fun DevSpaceTheme(
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
