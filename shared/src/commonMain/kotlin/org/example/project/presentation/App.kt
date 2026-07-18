package org.example.project.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.example.project.presentation.theme.*
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinContext

@Composable
fun App() {
    val themeMode = remember { mutableStateOf(ThemeMode.DEFAULT) }

    KoinContext {
        CompositionLocalProvider(
            LocalSetAccentColor provides { color: Color ->
                CodeNestColors.updateAccent(color)
            },
            LocalThemeMode provides themeMode.value,
            LocalSetThemeMode provides { mode: ThemeMode ->
                themeMode.value = mode
                CodeNestColors.updateTheme(mode == ThemeMode.DARK)
            }
        ) {
            CodeNestTheme(
                themeMode = themeMode.value
            ) {
                Navigator(CodeNestLoginScreen())
            }
        }
    }
}
