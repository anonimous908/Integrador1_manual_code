package org.example.project

import androidx.compose.ui.window.Window
import org.example.project.presentation.App
import androidx.compose.ui.window.application

fun main() = application {
    org.example.project.di.initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject",
    ) {
        App()
    }
}