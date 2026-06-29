package org.example.project.presentation

import androidx.compose.runtime.*
import org.example.project.presentation.theme.CodeNestTheme
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        CodeNestTheme {
            Navigator(CodeNestLoginScreen())
        }
    }
}
