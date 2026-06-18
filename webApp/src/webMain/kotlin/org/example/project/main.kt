package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import org.example.project.presentation.App
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    setupDependencyInjection()
    launchWebApp()
}

private fun setupDependencyInjection() {
    try {
        org.example.project.di.initKoin()
    } catch (e: Exception) {
        // Ya inicializado
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun launchWebApp() {
    ComposeViewport {
        App()
    }
}