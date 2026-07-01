package org.example.project.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.example.project.presentation.theme.*
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinContext

@Composable
fun App() {
    val accentColor = remember { mutableStateOf(CodeNestColors.primary) }
    val themeMode = remember { mutableStateOf(ThemeMode.DARK) }

    KoinContext {
        CompositionLocalProvider(
            LocalAccentColor provides accentColor.value,
            LocalOnAccentColor provides CodeNestColors.onPrimary,
            LocalSetAccentColor provides { color: Color ->
                accentColor.value = color
                CodeNestColors.updateAccent(color)
            },
            LocalThemeMode provides themeMode.value,
            LocalSetThemeMode provides { mode: ThemeMode ->
                themeMode.value = mode
                CodeNestColors.updateTheme(mode != ThemeMode.LIGHT)
            }
        ) {
            CodeNestTheme(
                accentColor = accentColor.value,
                onAccentColor = CodeNestColors.onPrimary,
                themeMode = themeMode.value
            ) {
                Navigator(CodeNestLoginScreen())
            }
        }
    }
}
