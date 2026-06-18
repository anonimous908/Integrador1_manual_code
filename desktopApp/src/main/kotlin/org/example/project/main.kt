package org.example.project

import androidx.compose.ui.window.Window
import org.example.project.presentation.App
import androidx.compose.ui.window.application

fun main() {
    setupDependencyInjection()
    launchDesktopApp()
}

private fun setupDependencyInjection() {
    try {
        org.example.project.di.initKoin()
    } catch (e: Exception) {
        // Ya inicializado
    }
}

private fun launchDesktopApp() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CodeNest",
    ) {
        App()
    }
}