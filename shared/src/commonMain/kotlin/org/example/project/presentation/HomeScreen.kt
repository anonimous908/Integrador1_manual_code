package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.project.presentation.theme.DevSpaceColors
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class HomeScreen(val email: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "DevSpace",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DevSpaceColors.background,
                        titleContentColor = DevSpaceColors.onSurface
                    )
                )
            },
            containerColor = DevSpaceColors.background
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val currentMaxWidth = maxWidth
                val scaleFactor = remember(currentMaxWidth) { (currentMaxWidth / 480.dp).coerceIn(0.5f, 1.5f) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp * scaleFactor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Glassmorphism card
                    Column(
                        modifier = Modifier
                            .widthIn(max = (currentMaxWidth * 0.85f).coerceIn(320.dp, 500.dp))
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(24.dp * scaleFactor),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp * scaleFactor)
                    ) {
                        Text(
                            text = "Bienvenido",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )

                        Text(
                            text = email,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp * scaleFactor))

                        OutlinedButton(
                            onClick = { navigator.replace(DevSpaceLoginScreen()) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp * scaleFactor),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Icon(
                            imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Cerrar sesión",
                                modifier = Modifier.size(18.dp * scaleFactor)
                            )
                            Spacer(modifier = Modifier.width(8.dp * scaleFactor))
                            Text(
                                text = "Cerrar sesión",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

