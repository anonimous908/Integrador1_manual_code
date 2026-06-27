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
import org.example.project.domain.model.SnippetCardData
import org.example.project.domain.service.SyntaxHighlighter
import org.koin.compose.koinInject

class AISearchScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val syntaxHighlighter = koinInject<SyntaxHighlighter>()
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
                                delay(1500) // Simulación de retraso de la API
                                isSearching = false
                                showResult = true
                                hasMatch = true
                                
                                aiExplanation = "Analizando tu base de datos de recetas locales para resolver tu consulta: '$query'.\n\n[PROTOTIPO]: En la siguiente fase, esta consulta se enviará a la API de DeepSeek (con temperatura de 0.2 para alta fidelidad) para analizar semánticamente tus códigos y adaptarlos a lo que necesitas."
                                suggestedSnippet = SnippetCardData(
                                    title = "Sugerencia para: $query",
                                    languageTag = "AI Output",
                                    secondaryTag = "Simulado",
                                    iconAccent = DevSpaceColors.primaryContainer,
                                    activeLineIndex = 2,
                                    footerLabel = "Generado por DeepSeek",
                                    footerIcon = Icons.Filled.Visibility,
                                    codeLines = listOf(
                                        "// Este es un prototipo de cómo se mostrará el código adaptado",
                                        "// para tu búsqueda: \"$query\"",
                                        "fun main() {",
                                        "    println(\"DeepSeek procesará esta consulta de forma dinámica\")",
                                        "}"
                                    )
                                )
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
                            SnippetCard(
                                card = snippet,
                                syntaxHighlighter = syntaxHighlighter,
                                onCopied = {
                                    showCopiedNotice = true
                                }
                            )

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
