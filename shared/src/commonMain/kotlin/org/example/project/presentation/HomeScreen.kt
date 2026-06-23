package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.example.project.presentation.theme.DevSpaceColors
import org.example.project.presentation.components.DevSpaceSidebar
import org.example.project.presentation.components.WelcomeContent
import org.example.project.presentation.components.PlaceholderContent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class HomeScreen(val email: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf("My Recipes") }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(DevSpaceColors.background)
        ) {
            // Sidebar
            DevSpaceSidebar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                email = email,
                onLogout = { navigator.replace(DevSpaceLoginScreen()) }
            )

            // Main Content Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(DevSpaceColors.background)
            ) {
                when (selectedTab) {
                    "My Recipes" -> WelcomeContent(email)
                    "Community Examples" -> PlaceholderContent("Estás en la pantalla de Manuales Compartidos")
                    "Recent" -> PlaceholderContent("Estás en la pantalla de Recientes")
                    "Settings" -> PlaceholderContent("Estás en la pantalla de Configuración")
                    "Documentation" -> PlaceholderContent("Estás en la pantalla de Documentación")
                    "Support" -> PlaceholderContent("Estás en la pantalla de Soporte")
                    else -> PlaceholderContent("Pantalla desconocida")
                }
            }
        }
    }
}

