package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.components.CodeNestSidebar
import org.example.project.presentation.components.WelcomeContent
import org.example.project.presentation.components.PlaceholderContent
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.project.presentation.tabs.*

class HomeScreen(val email: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        TabNavigator(MyRecipesTab(email)) { tabNavigator ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CodeNestColors.background)
            ) {
                // Sidebar
                CodeNestSidebar(
                    tabNavigator = tabNavigator,
                    email = email,
                    onLogout = { navigator.replace(CodeNestLoginScreen()) },
                    onNewSnippet = { tabNavigator.current = SnippetDetailTab(email) }
                )

                // Main Content Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(CodeNestColors.background)
                ) {
                    CurrentTab()
                }
            }
        }
    }
}

