package org.example.project

import androidx.compose.ui.ExperimentalComposeUiApi
import org.example.project.presentation.App
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App()
    }
}