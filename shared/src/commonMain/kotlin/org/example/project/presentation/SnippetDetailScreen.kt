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
import org.example.project.domain.service.LanguageDetector
import org.example.project.domain.repository.RecipeRepository
import org.example.project.data.network.AiApiService
import org.example.project.platform.rememberImagePickerLauncher
import org.koin.compose.koinInject
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/* ---------------------------------------------------------
 * MODELOS
 * --------------------------------------------------------- */
@Serializable
data class CodeLine(val number: Int, val content: String)

@Serializable
data class SnippetDetail(
    val breadcrumb: List<String> = listOf("Mis Recetas", "Python", "Interfaz Gráfica"),
    val title: String = "Botón con Tkinter",
    val language: String = "Python",
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
        val recipeRepository = koinInject<RecipeRepository>()
        val coroutineScope = rememberCoroutineScope()
        var isEditing by remember { mutableStateOf(snippet == emptySnippetDetail) }
        var currentSnippet by remember { mutableStateOf(snippet) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeNestColors.background)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderBar(
                snippet = currentSnippet,
                isEditing = isEditing,
                onSnippetChange = { currentSnippet = it },
                onEditToggle = { isEditing = !isEditing },
                onSave = {
                    coroutineScope.launch {
                        recipeRepository.saveRecipe(currentSnippet)
                        isEditing = false
                    }
                },
                onBack = { tabNavigator.current = org.example.project.presentation.tabs.MyRecipesTab(email) },
                onShare = { /* TODO */ },
                onDelete = {
                    coroutineScope.launch {
                        recipeRepository.deleteRecipe(currentSnippet.title)
                        tabNavigator.current = org.example.project.presentation.tabs.MyRecipesTab(email)
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Columna izquierda: código (2/3)
                Column(modifier = Modifier.weight(2f)) {
                    TagsAndMeta(currentSnippet, isEditing) { currentSnippet = it }
                    Spacer(Modifier.height(16.dp))
                    CodeBlockCard(currentSnippet, isEditing) { currentSnippet = it }
                }

                // Columna derecha: contexto/evidencia (1/3)
                Column(modifier = Modifier.weight(1f)) {
                    DescriptionCard(currentSnippet, isEditing) { currentSnippet = it }
                    Spacer(Modifier.height(24.dp))
                    EvidenceCard(currentSnippet, isEditing, { currentSnippet = it }, { code, lang -> 
                        var updated = currentSnippet.withRawCode(code)
                        if (lang.isNotBlank()) {
                            val ext = LanguageDetector.getDefaultExtension(lang)
                            updated = updated.copy(language = lang, fileName = "archivo.$ext")
                        }
                        currentSnippet = updated
                    })
                }
            }
        }
    }
}

@Composable
private fun HeaderBar(
    snippet: SnippetDetail,
    isEditing: Boolean,
    onSnippetChange: (SnippetDetail) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeNestColors.background.copy(alpha = 0.9f))
            .border(width = 1.dp, color = CodeNestColors.outlineVariant.copy(alpha = 0.5f))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.padding(end = 16.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = CodeNestColors.onSurface)
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    snippet.breadcrumb.forEachIndexed { index, crumb ->
                        Text(
                            text = crumb,
                            color = if (index == snippet.breadcrumb.lastIndex) CodeNestColors.onSurface else CodeNestColors.onSurfaceVariant,
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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GhostButton(
                icon = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                label = if (isEditing) "Guardar" else "Editar",
                onClick = if (isEditing) { { onSave(); onEditToggle() } } else onEditToggle
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
                    placeholder = { Text("#etiquetas separadas por espacio") },
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
                            .clip(RoundedCornerShape(4.dp))
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

    fun updateCode(newCode: String) {
        var updated = snippet.withRawCode(newCode)
        // Si el código se borró, resetear lenguaje y archivo
        if (newCode.isBlank()) {
            updated = updated.copy(language = "", fileName = "archivo.ext")
        } else if (updated.language.isBlank()) {
            val detected = LanguageDetector.detectFromFileName(updated.fileName)
                ?: LanguageDetector.detectFromCode(newCode)
            if (detected != null) {
                val ext = LanguageDetector.getDefaultExtension(detected)
                val baseName = if (updated.fileName == "archivo.ext") "archivo" else updated.fileName.substringBeforeLast('.')
                updated = updated.copy(language = detected, fileName = "$baseName.$ext")
            }
        }
        onSnippetChange(updated)
    }

    fun updateFileName(newName: String) {
        val newExt = snippet.language.let { lang ->
            if (lang.isNotBlank()) LanguageDetector.getDefaultExtension(lang) else ""
        }
        val newFileName = if (newExt.isNotEmpty() && !newName.endsWith(".$newExt")) {
            val base = newName.substringBeforeLast('.')
            if (base.isNotEmpty()) "$base.$newExt" else newName
        } else newName
        var updated = snippet.copy(fileName = newFileName)
        val detected = LanguageDetector.detectFromFileName(newFileName)
        if (detected != null && updated.language.isBlank()) {
            updated = updated.copy(language = detected)
        }
        onSnippetChange(updated)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 500.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CodeNestColors.GlassPanel)
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
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
                        onValueChange = { updateFileName(it) },
                        textStyle = TextStyle(color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(snippet.fileName, color = CodeNestColors.onSurface, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        if (snippet.language.isNotBlank()) {
                            Box(
                                modifier = Modifier.background(CodeNestColors.primary.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(snippet.language, color = CodeNestColors.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                onValueChange = { updateCode(it) },
                modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp),
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = CodeNestColors.onSurface),
                placeholder = { Text("Pega o escribe tu código aquí...", fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
            if (snippet.language.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CodeNestColors.primary, modifier = Modifier.size(14.dp))
                    Text("Detectado: ${snippet.language}", color = CodeNestColors.primary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
        } else {
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
                                text = highlightCode(line.content, snippet.language),
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

private fun highlightCode(line: String, language: String) = buildAnnotatedString {
    val pink = Color(0xFFFF79C6)   // keywords
    val yellow = Color(0xFFF1FA8C) // strings
    val comment = Color(0xFF6272A4) // comments
    val teal = Color(0xFF8BE9FD)    // types/classes
    val orange = Color(0xFFFFB86C)  // numbers
    val green = Color(0xFF50FA7B)   // functions
    val default = Color(0xFFF8F8F2)

    val keywords = when {
        language.equals("Kotlin", ignoreCase = true) -> setOf("fun", "val", "var", "class", "object", "interface", "sealed", "data", "enum", "when", "if", "else", "for", "while", "do", "return", "break", "continue", "in", "is", "as", "null", "true", "false", "import", "package", "throw", "try", "catch", "finally", "suspend", "this", "super", "typealias", "companion", "internal", "private", "protected", "public", "override", "abstract", "open", "final", "const", "lateinit", "by", "dynamic", "typeof")
        language.equals("Python", ignoreCase = true) -> setOf("def", "class", "if", "elif", "else", "for", "while", "import", "from", "as", "return", "yield", "raise", "try", "except", "finally", "with", "lambda", "pass", "break", "continue", "and", "or", "not", "is", "in", "True", "False", "None", "self", "global", "nonlocal", "assert", "del", "async", "await")
        language.equals("Java", ignoreCase = true) -> setOf("public", "private", "protected", "static", "final", "abstract", "class", "interface", "enum", "extends", "implements", "new", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "throw", "throws", "try", "catch", "finally", "import", "package", "this", "super", "null", "true", "false", "void", "int", "String", "boolean", "long", "double", "float", "byte", "char", "short", "synchronized", "volatile", "transient", "native", "strictfp", "default")
        language.equals("JavaScript", ignoreCase = true) || language.equals("TypeScript", ignoreCase = true) -> setOf("function", "const", "let", "var", "class", "extends", "new", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "try", "catch", "finally", "throw", "import", "export", "default", "from", "as", "async", "await", "yield", "this", "super", "null", "undefined", "true", "false", "typeof", "instanceof", "in", "of", "delete", "void", "static", "get", "set", "interface", "type", "enum", "implements", "abstract")
        language.equals("C", ignoreCase = true) || language.equals("C++", ignoreCase = true) -> setOf("auto", "break", "case", "const", "continue", "default", "do", "else", "enum", "extern", "for", "goto", "if", "register", "return", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "class", "namespace", "using", "template", "typename", "public", "private", "protected", "virtual", "override", "new", "delete", "this", "nullptr", "true", "false", "try", "catch", "throw", "include", "define")
        language.equals("Go", ignoreCase = true) -> setOf("func", "var", "const", "type", "struct", "interface", "map", "chan", "package", "import", "return", "if", "else", "for", "range", "switch", "case", "break", "continue", "go", "defer", "select", "goto", "fallthrough", "nil", "true", "false")
        language.equals("Rust", ignoreCase = true) -> setOf("fn", "let", "mut", "const", "static", "struct", "enum", "trait", "impl", "mod", "use", "pub", "crate", "self", "super", "match", "if", "else", "for", "while", "loop", "in", "break", "continue", "return", "where", "as", "ref", "move", "unsafe", "extern", "type", "dyn", "true", "false", "Some", "None", "Ok", "Err")
        language.equals("C#", ignoreCase = true) -> setOf("using", "namespace", "class", "struct", "interface", "enum", "record", "public", "private", "protected", "internal", "static", "readonly", "const", "virtual", "override", "abstract", "sealed", "async", "await", "var", "new", "return", "if", "else", "for", "foreach", "while", "do", "switch", "case", "break", "continue", "try", "catch", "finally", "throw", "null", "true", "false", "this", "base", "in", "out", "ref", "params", "yield", "lock", "fixed")
        else -> setOf("import", "as", "def", "function", "class", "return", "if", "else", "for", "while", "var", "let", "const")
    }

    val types = when {
        language.equals("Kotlin", ignoreCase = true) -> setOf("Int", "Long", "Float", "Double", "Boolean", "Byte", "Short", "Char", "String", "Any", "Unit", "Nothing", "List", "MutableList", "Set", "MutableSet", "Map", "MutableMap", "Array", "IntArray", "Sequence")
        language.equals("Java", ignoreCase = true) -> setOf("String", "Integer", "Double", "Float", "Boolean", "Long", "Byte", "Short", "Character", "Object", "List", "ArrayList", "Map", "HashMap", "Set", "HashSet")
        language.equals("TypeScript", ignoreCase = true) -> setOf("string", "number", "boolean", "void", "any", "never", "unknown", "null", "undefined", "object", "Array", "Map", "Set", "Promise", "Record", "Partial", "Required", "Readonly", "Pick", "Omit")
        else -> emptySet()
    }

    val tokens = tokenize(line, language)
    tokens.forEach { token ->
        val trimmed = token.trim()
        val style = when {
            token.startsWith("\"") || token.startsWith("'") || token.startsWith("`") -> SpanStyle(color = yellow)
            token.startsWith("//") || token.startsWith("#") -> SpanStyle(color = comment)
            trimmed in keywords -> SpanStyle(color = pink)
            trimmed in types -> SpanStyle(color = teal)
            trimmed.toDoubleOrNull() != null || trimmed.toIntOrNull() != null -> SpanStyle(color = orange)
            else -> SpanStyle(color = default)
        }
        withStyle(style) { append(token) }
    }
}

private fun tokenize(line: String, language: String): List<String> {
    val result = mutableListOf<String>()
    val chars = line.toCharArray()
    var i = 0
    val sb = StringBuilder()

    while (i < chars.size) {
        val c = chars[i]
        when {
            c == '"' || c == '\'' || c == '`' -> {
                if (sb.isNotEmpty()) { result.add(sb.toString()); sb.clear() }
                val quote = c
                sb.append(quote)
                i++
                while (i < chars.size && chars[i] != quote) {
                    if (chars[i] == '\\' && i + 1 < chars.size) { sb.append(chars[i]); i++; sb.append(chars[i]) }
                    else { sb.append(chars[i]) }
                    i++
                }
                if (i < chars.size) { sb.append(chars[i]); i++ }
                result.add(sb.toString()); sb.clear()
            }
            c == '/' && i + 1 < chars.size && chars[i + 1] == '/' -> {
                if (sb.isNotEmpty()) { result.add(sb.toString()); sb.clear() }
                result.add(line.substring(i)); return result
            }
            c == '#' && language.equals("Python", ignoreCase = true) -> {
                if (sb.isNotEmpty()) { result.add(sb.toString()); sb.clear() }
                result.add(line.substring(i)); return result
            }
            c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' ||
            c == ';' || c == ',' || c == ':' || c == '.' -> {
                if (sb.isNotEmpty()) { result.add(sb.toString()); sb.clear() }
                result.add(c.toString()); i++
            }
            c == ' ' || c == '\t' -> {
                if (sb.isNotEmpty()) { result.add(sb.toString()); sb.clear() }
                sb.append(c)
                while (i + 1 < chars.size && (chars[i + 1] == ' ' || chars[i + 1] == '\t')) {
                    sb.append(chars[i + 1]); i++
                }
                result.add(sb.toString()); sb.clear(); i++
            }
            else -> {
                sb.append(c); i++
            }
        }
    }
    if (sb.isNotEmpty()) result.add(sb.toString())
    return result
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
private fun EvidenceCard(
    snippet: SnippetDetail,
    isEditing: Boolean,
    onSnippetChange: (SnippetDetail) -> Unit,
    onCodeChange: (String, String) -> Unit
) {
    val aiService = koinInject<AiApiService>()
    val coroutineScope = rememberCoroutineScope()
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var analyzing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    val pickImage = rememberImagePickerLauncher { bytes ->
        imageBytes = bytes
        statusMessage = "Imagen cargada (${bytes.size / 1024} KB). Escribe qué quieres hacer y presiona 'Analizar con IA'."
    }

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
                placeholder = { Text("Describe qué quieres hacer con esta imagen...") },
                minLines = 2
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { pickImage() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.surfaceContainerHigh)
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(if (imageBytes != null) "Cambiar Imagen" else "Subir Imagen", fontSize = 12.sp)
                }
                if (imageBytes != null) {
                    Button(
                        onClick = {
                            analyzing = true
                            statusMessage = "Analizando imagen con IA..."
                            coroutineScope.launch {
                                val result = aiService.transcribeCodeFromImage(
                                    imageBytes!!,
                                    snippet.evidenceText.ifBlank { "Transcribe el código de esta imagen" }
                                )
                                result.onSuccess { transcription ->
                                    onCodeChange(transcription.code, transcription.language)
                                    statusMessage = if (transcription.language.isNotBlank())
                                        "Detectado: ${transcription.language}. Código transcrito."
                                    else "Código transcrito exitosamente."
                                    imageBytes = null
                                }.onFailure { e ->
                                    statusMessage = "Error: ${e.message}"
                                }
                                analyzing = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !analyzing,
                        colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.primary, contentColor = CodeNestColors.onPrimary)
                    ) {
                        Icon(if (analyzing) Icons.Default.Refresh else Icons.Default.AutoAwesome, contentDescription = null, modifier = if (analyzing) Modifier.size(16.dp) else Modifier)
                        Spacer(Modifier.width(6.dp))
                        Text(if (analyzing) "Analizando..." else "Analizar con IA", fontSize = 12.sp)
                    }
                }
            }
            if (statusMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(statusMessage!!,                 color = if (statusMessage!!.startsWith("Error")) CodeNestColors.error else CodeNestColors.primary, fontSize = 11.sp)
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
    language = "",
    tags = emptyList(),
    createdDate = "Hoy",
    usageCount = 0,
    fileName = "archivo.ext",
    description = "",
    dependencies = "",
    evidenceText = "",
    code = emptyList()
)
