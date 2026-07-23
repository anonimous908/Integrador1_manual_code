package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.project.presentation.grabado_online.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.base.rememberTabOptions
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.domain.model.CodeLine
import org.example.project.domain.model.SnippetDetail
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.domain.repository.RecipeRepository
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

/* ---------------------------------------------------------
 * PANTALLA PRINCIPAL (detalle de snippet)
 * --------------------------------------------------------- */
class SnippetDetailTab(
    val email: String,
    val snippet: SnippetDetail = SnippetDetail.emptySnippetDetail,
    val recipeId: String? = null
) : Tab {

    override val options: TabOptions
        @Composable
        get() = rememberTabOptions(
            index = 99u,
            title = "Detail",
            icon = Icons.Default.Code
        )

    @Composable
    override fun Content() {
        val tabNavigator = LocalTabNavigator.current
        val recipeRepository = koinInject<RecipeRepository>()
        val scope = rememberCoroutineScope()
        var isEditing by remember { mutableStateOf(snippet == SnippetDetail.emptySnippetDetail) }
        var currentSnippet by remember { mutableStateOf(snippet) }
        var saveError by remember { mutableStateOf<String?>(null) }
        val grabadoViewModel = remember { GrabadoOnlineViewModel() }
        val grabadoState by grabadoViewModel.state.collectAsState()

        val onSaveRecipe: () -> Unit = {
            scope.launch {
                try {
                    val recipe = currentSnippet.toRecipe(email, recipeId)
                    if (recipeId != null) {
                        recipeRepository.update(recipe)
                    } else {
                        recipeRepository.create(recipe)
                    }
                    tabNavigator.current = org.example.project.presentation.tabs.MyRecipesTab(email)
                } catch (e: Exception) {
                    saveError = e.message ?: "Error saving recipe"
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeNestColors.background)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderBar(
                snippet = currentSnippet,
                isEditing = isEditing,
                grabadoState = grabadoState,
                onToggleGrabado = { grabadoViewModel.onEvent(GrabadoOnlineEvent.ToggleRecording) },
                onSnippetChange = { currentSnippet = it },
                onEditToggle = { isEditing = !isEditing },
                onSaveRecipe = onSaveRecipe,
                onBack = { tabNavigator.current = org.example.project.presentation.tabs.MyRecipesTab(email) },
                onShare = {},
                onDelete = {}
            )

            // Save error snackbar
            if (saveError != null) {
                LaunchedEffect(saveError) {
                    kotlinx.coroutines.delay(3000)
                    saveError = null
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(CodeNestColors.error.copy(alpha = 0.2f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        saveError ?: "",
                        color = CodeNestColors.onSurface,
                        fontSize = 13.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Columna izquierda: código (2/3)
                Column(modifier = Modifier.weight(2f)) {
                    LaunchedEffect(grabadoState.latestOcrCode) {
                        val ocr = grabadoState.latestOcrCode
                        if (!ocr.isNullOrBlank() && grabadoState.isRecording) {
                            currentSnippet = currentSnippet.withRawCode(ocr).copy(fileName = "pizarra_captura.py")
                        }
                    }
                    if (grabadoState.isRecording) {
                        OcrSyncBanner(
                            latestOcrCode = grabadoState.latestOcrCode,
                            onReceiveOcrCapture = { ocrText ->
                                currentSnippet = currentSnippet.withRawCode(ocrText).copy(fileName = "pizarra_captura.py")
                            }
                        )
                    }
                    TagsAndMeta(currentSnippet, isEditing) { currentSnippet = it }
                    Spacer(Modifier.height(16.dp))
                    CodeBlockCard(currentSnippet, isEditing) { currentSnippet = it }
                }

                // Columna derecha: contexto/evidencia (1/3)
                Column(modifier = Modifier.weight(1f)) {
                    DescriptionCard(currentSnippet, isEditing) { currentSnippet = it }
                    Spacer(Modifier.height(24.dp))
                    EvidenceCard(currentSnippet, isEditing, onSnippetChange = { currentSnippet = it })
                }
            }
        }
    }
}

@Composable
private fun HeaderBar(
    snippet: SnippetDetail,
    isEditing: Boolean,
    grabadoState: GrabadoOnlineState,
    onToggleGrabado: () -> Unit,
    onSnippetChange: (SnippetDetail) -> Unit,
    onEditToggle: () -> Unit,
    onSaveRecipe: () -> Unit,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeNestColors.background.copy(alpha = 0.9f))
            .border(width = 1.dp, color = CodeNestColors.outlineVariant.copy(alpha = 0.5f))
            .padding(horizontal = 32.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = CodeNestColors.onSurface)
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    snippet.breadcrumb.forEachIndexed { index, item ->
                        Text(
                            text = item,
                            color = if (index == snippet.breadcrumb.lastIndex) CodeNestColors.primary else CodeNestColors.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        if (index != snippet.breadcrumb.lastIndex) {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = CodeNestColors.onSurfaceVariant,
                                modifier = Modifier.size(14.dp).padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                
                if (isEditing) {
                    OutlinedTextField(
                        value = snippet.title,
                        onValueChange = { onSnippetChange(snippet.copy(title = it)) },
                        textStyle = TextStyle(color = CodeNestColors.onSurface, fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(0.5f),
                        placeholder = { Text("Título de la receta") }
                    )
                } else {
                    Text(
                        text = snippet.title,
                        color = CodeNestColors.onSurface,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            GrabadoOnlineButton(
                state = grabadoState,
                onToggle = onToggleGrabado
            )
            if (isEditing) {
                GhostButton(
                    icon = Icons.Default.Check,
                    label = "Guardar",
                    onClick = onSaveRecipe
                )
            } else {
                GhostButton(
                    icon = Icons.Default.Edit,
                    label = "Editar",
                    onClick = onEditToggle
                )
            }
            GhostButton(icon = Icons.Default.Share, label = "Compartir", onClick = onShare)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = CodeNestColors.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun GhostButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CodeNestColors.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CodeNestColors.onSurface)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
private fun TagsAndMeta(snippet: SnippetDetail, isEditing: Boolean, onSnippetChange: (SnippetDetail) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (isEditing) {
                OutlinedTextField(
                    value = snippet.tags.joinToString(" "),
                    onValueChange = { 
                        val newTags = it.split(" ").filter { tag -> tag.isNotBlank() }
                        onSnippetChange(snippet.copy(tags = newTags)) 
                    },
                    placeholder = { Text("#etiquetas (ej. #Python #Tkinter)") },
                    textStyle = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace),
                    modifier = Modifier.width(300.dp)
                )
            } else {
                snippet.tags.forEach { tag ->
                    val (bg, fg) = when {
                        tag.contains("Python") -> CodeNestColors.tertiary.copy(alpha = 0.15f) to CodeNestColors.tertiary
                        tag.contains("Tkinter") -> CodeNestColors.primary.copy(alpha = 0.10f) to CodeNestColors.primary
                        else -> CodeNestColors.secondary.copy(alpha = 0.10f) to CodeNestColors.secondary
                    }
                    Text(
                        text = tag,
                        color = fg,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(bg)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
        if (!isEditing) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MetaItem(Icons.Default.CalendarToday, snippet.createdDate)
                MetaItem(Icons.Default.ContentCopy, "${snippet.usageCount} usos")
            }
        }
    }
}

@Composable
private fun MetaItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, color = CodeNestColors.onSurfaceVariant, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun CodeBlockCard(snippet: SnippetDetail, isEditing: Boolean, onSnippetChange: (SnippetDetail) -> Unit) {
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 500.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CodeNestColors.GlassPanel)
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CodeNestColors.surfaceContainerHighest)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Code, contentDescription = null, tint = CodeNestColors.primary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                if (isEditing) {
                    BasicTextField(
                        value = snippet.fileName,
                        onValueChange = { onSnippetChange(snippet.copy(fileName = it)) },
                        textStyle = TextStyle(color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(snippet.fileName, color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        val langTag = snippet.tags.firstOrNull { !it.startsWith("#") } ?: snippet.tags.firstOrNull()?.removePrefix("#")
                        if (langTag != null) {
                            Box(
                                modifier = Modifier.background(CodeNestColors.primary.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(langTag, color = CodeNestColors.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            if (!isEditing) {
                Button(
                    onClick = { copied = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (copied) CodeNestColors.secondary else CodeNestColors.primary,
                        contentColor = if (copied) CodeNestColors.onSecondary else CodeNestColors.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(if (copied) "¡Copiado!" else "Copia Limpia", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (isEditing) {
            OutlinedTextField(
                value = snippet.rawCode,
                onValueChange = { onSnippetChange(snippet.withRawCode(it)) },
                modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp),
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = CodeNestColors.onSurface),
                placeholder = { Text("Pega o escribe tu código aquí...", fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        } else {
            val syntaxHighlighter = koinInject<SyntaxHighlighter>()
            val langTag = snippet.tags
                .firstOrNull { !it.startsWith("#") }
                ?.removePrefix("#")
                ?: snippet.tags.firstOrNull()?.removePrefix("#")
                ?: ""
            // Código con números de línea (selección habilitada para copiar manualmente)
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .background(CodeNestColors.codeBg)
                        .padding(vertical = 16.dp)
                ) {
                    snippet.code.forEach { line ->
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            Text(
                                text = line.number.toString(),
                                color = CodeNestColors.tertiary.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.width(40.dp)
                            )
                            Text(
                                text = syntaxHighlighter.highlight(line.content, langTag),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DescriptionCard(snippet: SnippetDetail, isEditing: Boolean, onSnippetChange: (SnippetDetail) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        color = CodeNestColors.GlassPanel
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(icon = Icons.Default.Info, title = "Sobre esta Receta", tint = CodeNestColors.primary)
            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Generar con IA", tint = CodeNestColors.primary)
            }
        }
        
        if (isEditing) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = snippet.description,
                onValueChange = { onSnippetChange(snippet.copy(description = it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Escribe una descripción...") },
                minLines = 3
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = snippet.dependencies,
                onValueChange = { onSnippetChange(snippet.copy(dependencies = it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Dependencias (ej. Python 3.x)") }
            )
        } else {
            if (snippet.description.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(snippet.description, color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, lineHeight = 20.sp)
            }
            
            if (snippet.dependencies.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(CodeNestColors.surfaceContainer)
                        .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text("Dependencias:", color = CodeNestColors.tertiary, fontSize = 12.sp)
                    Text(snippet.dependencies, color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
        }
    }
}

@Composable
private fun EvidenceCard(
    snippet: SnippetDetail,
    isEditing: Boolean,
    onSnippetChange: (SnippetDetail) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        color = CodeNestColors.GlassPanel
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
        SectionHeader(icon = Icons.Default.Verified, title = "Evidencia", tint = CodeNestColors.secondary)

        if (isEditing) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = snippet.evidenceText,
                onValueChange = { onSnippetChange(snippet.copy(evidenceText = it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Describe la evidencia...") },
                minLines = 2
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.surfaceContainerHigh)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Subir Imagen")
            }
        } else {
            // Mostrar solo si no es un snippet nuevo
            if (snippet.evidenceText.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    snippet.evidenceText,
                    color = CodeNestColors.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(16.dp))
                // Reemplazar por Coil/Image multiplataforma cargando la captura real
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CodeNestColors.surfaceContainerHigh)
                        .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(32.dp))
                }
            }
        }
        }
    }
}

@Composable
private fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}


