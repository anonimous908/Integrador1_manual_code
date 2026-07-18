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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.grabado_online.PlatformGrabadoOnlineButton
import org.example.project.presentation.grabado_online.PlatformGrabadoOnlineBanner

/* ---------------------------------------------------------
 * MODELOS
 * --------------------------------------------------------- */
data class CodeLine(val number: Int, val content: String)

data class SnippetDetail(
    val breadcrumb: List<String> = listOf("Mis Recetas", "Python", "Interfaz Gráfica"),
    val title: String = "Botón con Tkinter",
    val tags: List<String> = listOf("#Python", "#Tkinter", "#GUI"),
    val createdDate: String = "24 oct. 2023",
    val usageCount: Int = 142,
    val fileName: String = "main.py",
    val description: String = "Una implementación básica de una interfaz gráfica usando " +
        "Tkinter en Python, que incluye un botón funcional y una etiqueta de estado.",
    val dependencies: String = "Python (3.x)",
    val evidenceText: String = "Captura de pantalla de la aplicación Tkinter ejecutándose, mostrando el botón y el mensaje de estado tras ser presionado.",
    val code: List<CodeLine>
) {
    val rawCode: String get() = code.joinToString("\n") { it.content }

    fun withRawCode(newCode: String): SnippetDetail {
        val newLines = newCode.split("\n").mapIndexed { index, s -> CodeLine(index + 1, s) }
        return copy(code = newLines)
    }
}

/* ---------------------------------------------------------
 * PANTALLA PRINCIPAL (detalle de snippet)
 * --------------------------------------------------------- */
class SnippetDetailTab(
    val email: String,
    val snippet: SnippetDetail = emptySnippetDetail
) : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Detail"
            val icon = rememberVectorPainter(Icons.Default.Code)
            return remember {
                TabOptions(
                    index = 99u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val tabNavigator = LocalTabNavigator.current
        var isEditing by remember { mutableStateOf(snippet == emptySnippetDetail) }
        var currentSnippet by remember { mutableStateOf(snippet) }
        val grabadoViewModel = remember { GrabadoOnlineViewModel() }
        val grabadoState by grabadoViewModel.state.collectAsState()

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
                onBack = { tabNavigator.current = org.example.project.presentation.tabs.MyRecipesTab(email) },
                onShare = { /* TODO */ },
                onDelete = { /* TODO */ }
            )

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
                    PlatformGrabadoOnlineBanner(
                        state = grabadoState,
                        onOcrDetected = { ocrText ->
                            currentSnippet = currentSnippet.withRawCode(ocrText).copy(fileName = "pizarra_captura.py")
                        }
                    )
                    if (!isEditing) {
                        TagsAndMeta(currentSnippet, isEditing) { currentSnippet = it }
                        Spacer(Modifier.height(16.dp))
                    }
                    CodeBlockCard(currentSnippet, isEditing) { currentSnippet = it }
                }

                // Columna derecha: contexto/evidencia (1/3)
                Column(modifier = Modifier.weight(1f)) {
                    DescriptionCard(currentSnippet, isEditing) { currentSnippet = it }
                    Spacer(Modifier.height(24.dp))
                    EvidenceCard(currentSnippet, isEditing) { currentSnippet = it }
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
            PlatformGrabadoOnlineButton(
                state = grabadoState,
                onToggle = onToggleGrabado
            )
            GhostButton(
                icon = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                label = if (isEditing) "Guardar" else "Editar",
                onClick = onEditToggle
            )
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
        shape = RoundedCornerShape(4.dp),
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
    if (isEditing) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
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
                        .clip(RoundedCornerShape(4.dp))
                        .background(bg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetaItem(Icons.Default.CalendarToday, snippet.createdDate)
            MetaItem(Icons.Default.ContentCopy, "${snippet.usageCount} usos")
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
                    Text(snippet.fileName, color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
            if (!isEditing) {
                Button(
                    onClick = { copied = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (copied) CodeNestColors.secondary else CodeNestColors.primary,
                        contentColor = if (copied) CodeNestColors.onSecondary else CodeNestColors.onPrimary
                    ),
                    shape = RoundedCornerShape(4.dp),
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
                                text = highlightPython(line.content),
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

/** Resaltado de sintaxis muy simplificado, fiel a los tokens del mockup original. */
private fun highlightPython(line: String) = buildAnnotatedString {
    val keywords = setOf("import", "as", "def")
    val tokens = line.split(" ")
    tokens.forEachIndexed { i, token ->
        val style = when {
            token in keywords -> SpanStyle(color = Color(0xFFFF79C6))
            token.startsWith("\"") -> SpanStyle(color = Color(0xFFF1FA8C))
            token.startsWith("#") -> SpanStyle(color = Color(0xFF6272A4))
            else -> SpanStyle(color = Color(0xFFF8F8F2))
        }
        withStyle(style) { append(token) }
        if (i != tokens.lastIndex) append(" ")
    }
}

@Composable
private fun DescriptionCard(snippet: SnippetDetail, isEditing: Boolean, onSnippetChange: (SnippetDetail) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CodeNestColors.GlassPanel)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(icon = Icons.Default.Info, title = "Sobre esta Receta", tint = CodeNestColors.primary)
            IconButton(onClick = { /* TODO: IA action */ }, modifier = Modifier.size(24.dp)) {
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

@Composable
private fun EvidenceCard(snippet: SnippetDetail, isEditing: Boolean, onSnippetChange: (SnippetDetail) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CodeNestColors.GlassPanel)
            .padding(16.dp)
    ) {
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
                onClick = { /* TODO: Seleccionar imagen */ },
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

@Composable
private fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

/* ---------------------------------------------------------
 * PREVIEW / DATOS DE EJEMPLO
 * --------------------------------------------------------- */
private val sampleCode = listOf(
    CodeLine(1, "import tkinter as tk"),
    CodeLine(2, ""),
    CodeLine(3, "def al_hacer_clic():"),
    CodeLine(4, "    print(\"¡Botón presionado!\")"),
    CodeLine(5, "    etiqueta.config(text=\"Botón fue presionado\")"),
    CodeLine(6, ""),
    CodeLine(7, "# Crear ventana"),
    CodeLine(8, "ventana = tk.Tk()"),
    CodeLine(9, "ventana.title(\"Mi aplicación\")"),
    CodeLine(10, "ventana.geometry(\"300x200\")"),
    CodeLine(11, ""),
    CodeLine(12, "# Crear botón"),
    CodeLine(13, "boton = tk.Button("),
    CodeLine(14, "    ventana,"),
    CodeLine(15, "    text=\"Presióname\","),
    CodeLine(16, "    command=al_hacer_clic,"),
    CodeLine(17, "    bg=\"#007bff\","),
    CodeLine(18, "    fg=\"white\","),
    CodeLine(19, "    font=(\"Arial\", 12),"),
    CodeLine(20, "    padx=20,"),
    CodeLine(21, "    pady=10"),
    CodeLine(22, ")"),
    CodeLine(23, "boton.pack(pady=20)"),
    CodeLine(24, ""),
    CodeLine(25, "# Etiqueta para mostrar resultado"),
    CodeLine(26, "etiqueta = tk.Label(ventana, text=\"\", font=(\"Arial\", 10))"),
    CodeLine(27, "etiqueta.pack()"),
    CodeLine(28, ""),
    CodeLine(29, "ventana.mainloop()")
)

val sampleSnippetDetail = SnippetDetail(code = sampleCode)

val emptySnippetDetail = SnippetDetail(
    breadcrumb = listOf("Mis Recetas", "Nuevo Snippet"),
    title = "",
    tags = emptyList(),
    createdDate = "Hoy",
    usageCount = 0,
    fileName = "archivo.ext",
    description = "",
    dependencies = "",
    evidenceText = "",
    code = emptyList()
)
