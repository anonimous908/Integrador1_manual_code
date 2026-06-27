package org.example.project.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.presentation.theme.DevSpaceColors
import org.example.project.presentation.components.SnippetCard
import org.example.project.presentation.components.SnippetCardData

class AISearchScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        var query by remember { mutableStateOf("") }
        var isSearching by remember { mutableStateOf(false) }
        var showResult by remember { mutableStateOf(false) }
        var hasMatch by remember { mutableStateOf(false) }
        
        // Mensaje de estado de la IA
        var aiExplanation by remember { mutableStateOf("") }
        
        // Snippet recomendado/sugerido
        var suggestedSnippet by remember { mutableStateOf<SnippetCardData?>(null) }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DevSpaceColors.background)
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            // Header con botón de regresar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = DevSpaceColors.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = DevSpaceColors.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Búsqueda Semántica con IA (DeepSeek)",
                    color = DevSpaceColors.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Explicación de la herramienta
            Text(
                text = "Describe lo que necesitas en lenguaje natural. La IA buscará en tus recetas locales, adaptará fragmentos o sugerirá consultas.",
                color = DevSpaceColors.onSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Buscador de IA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DevSpaceColors.surfaceContainerLowest)
                    .border(width = 1.dp, color = DevSpaceColors.outlineVariant, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = DevSpaceColors.outline)
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            "Escribe tu petición (ej. 'botón en typescript' o 'cohortes')...",
                            color = DevSpaceColors.outline,
                            fontSize = 15.sp
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        singleLine = true,
                        textStyle = TextStyle(color = DevSpaceColors.onSurface, fontSize = 15.sp),
                        cursorBrush = SolidColor(DevSpaceColors.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (query.isNotBlank()) {
                            isSearching = true
                            showResult = false
                            coroutineScope.launch {
                                delay(1500) // Simulación de retraso de red de la API
                                isSearching = false
                                showResult = true
                                
                                val normalizedQuery = query.lowercase()
                                if (normalizedQuery.contains("boton") && normalizedQuery.contains("typescript")) {
                                    hasMatch = true
                                    aiExplanation = "Buscamos en tus recetas y no tienes un botón de TypeScript guardado. Sin embargo, encontramos tu receta de SQL 'Grupo A3' que gestiona analíticas de suscripciones. Como sugerencia inteligente de IA, hemos adaptado un botón de React TypeScript que consume dicha analítica:"
                                    suggestedSnippet = SnippetCardData(
                                        title = "Sugerencia: Analytics Button",
                                        languageTag = "TypeScript",
                                        secondaryTag = "AI Suggestion",
                                        iconAccent = DevSpaceColors.primaryContainer,
                                        activeLineIndex = 4,
                                        footerLabel = "Adapted from 'Grupo A3'",
                                        footerIcon = Icons.Filled.Visibility,
                                        codeLines = listOf(
                                            "import React, { useState } from 'react';",
                                            "",
                                            "interface AnalyticsButtonProps {",
                                            "  cohortMonth: string;",
                                            "  onFetchActiveUsers: (month: string) => Promise<number>;",
                                            "}",
                                            "",
                                            "export const AnalyticsButton: React.FC<AnalyticsButtonProps> = ({ cohortMonth, onFetchActiveUsers }) => {",
                                            "  const [loading, setLoading] = useState(false);",
                                            "  const [count, setCount] = useState<number | null>(null);",
                                            "",
                                            "  const handleClick = async () => {",
                                            "    setLoading(true);",
                                            "    const users = await onFetchActiveUsers(cohortMonth);",
                                            "    setCount(users);",
                                            "    setLoading(false);",
                                            "  };",
                                            "",
                                            "  return (",
                                            "    <button onClick={handleClick} disabled={loading} className=\"btn-primary\">",
                                            "      {loading ? 'Cargando...' : `Ver Usuarios (\${cohortMonth})`}",
                                            "      {count !== null && <span className=\"badge\">{count}</span>}",
                                            "    </button>",
                                            "  );",
                                            "};"
                                        )
                                    )
                                } else {
                                    hasMatch = false
                                    aiExplanation = "La API de IA no encontró coincidencias o adaptaciones lógicas para tu consulta en tus recetas SQL actuales. Intenta buscando 'botón en typescript' para ver la sugerencia adaptada."
                                    suggestedSnippet = null
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DevSpaceColors.primary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    enabled = !isSearching && query.isNotBlank()
                ) {
                    Text("Consultar IA", color = DevSpaceColors.onPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pantalla de carga
            if (isSearching) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = DevSpaceColors.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Procesando semántica con DeepSeek API...",
                            color = DevSpaceColors.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Resultados
            AnimatedVisibility(visible = showResult && !isSearching) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = DevSpaceColors.surfaceContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (hasMatch) "✨ Sugerencia Encontrada" else "⚠️ Sin Coincidencias Adaptables",
                                color = if (hasMatch) DevSpaceColors.secondary else DevSpaceColors.error,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = aiExplanation,
                                color = DevSpaceColors.onSurface,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    suggestedSnippet?.let { snippet ->
                        Text(
                            text = "Código Sugerido:",
                            color = DevSpaceColors.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        var showCopiedNotice by remember { mutableStateOf(false) }
                        
                        Box(modifier = Modifier.fillMaxWidth()) {
                            SnippetCard(card = snippet, onCopied = {
                                showCopiedNotice = true
                            })

                            if (showCopiedNotice) {
                                LaunchedEffect(Unit) {
                                    delay(2000)
                                    showCopiedNotice = false
                                }
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 16.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(DevSpaceColors.secondaryContainer)
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        "¡Código copiado al portapapeles!",
                                        color = DevSpaceColors.onSecondaryContainer,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
