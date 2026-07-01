package org.example.project.presentation.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.theme.LocalAccentColor
import org.example.project.presentation.theme.LocalSetAccentColor
import org.example.project.presentation.theme.LocalThemeMode
import org.example.project.presentation.theme.LocalSetThemeMode
import org.example.project.presentation.theme.ThemeMode
import org.example.project.data.network.AiConfigRepository
import org.koin.compose.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.graphics.vector.rememberVectorPainter

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.sidebar_settings)
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        SettingsContent()
    }
}

private enum class SettingsSection {
    PERSONALIZACION, TERMINAL_SHELL, SINCRONIZACION
}

@Composable
private fun SettingsContent() {
    var selectedSection by remember { mutableStateOf(SettingsSection.PERSONALIZACION) }
    var showDrawer by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(CodeNestColors.background)) {
        val isWide = maxWidth >= 600.dp
        val isNarrow = !isWide
        val sidePadding = if (isWide) 32.dp else 16.dp
        val topPadding = if (isWide) 32.dp else 12.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (isWide) {
                Column(
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                        .background(CodeNestColors.surfaceContainerLow)
                        .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.15f))
                        .padding(vertical = 24.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Configuración",
                        color = CodeNestColors.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    SettingsSubNavItem(
                        icon = Icons.Default.Palette,
                        label = "Personalización",
                        isActive = selectedSection == SettingsSection.PERSONALIZACION,
                        onClick = { selectedSection = SettingsSection.PERSONALIZACION }
                    )
                    SettingsSubNavItem(
                        icon = Icons.Default.Terminal,
                        label = "Terminal y Shell",
                        isActive = selectedSection == SettingsSection.TERMINAL_SHELL,
                        onClick = { selectedSection = SettingsSection.TERMINAL_SHELL }
                    )
                    SettingsSubNavItem(
                        icon = Icons.Default.CloudSync,
                        label = "Sincronización",
                        isActive = selectedSection == SettingsSection.SINCRONIZACION,
                        onClick = { selectedSection = SettingsSection.SINCRONIZACION }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (isNarrow) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth().background(CodeNestColors.surfaceContainerLow)
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { showDrawer = !showDrawer }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = CodeNestColors.onSurface)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Configuracion", color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = when (selectedSection) {
                                    SettingsSection.PERSONALIZACION -> "Personalizacion"
                                    SettingsSection.TERMINAL_SHELL -> "Terminal y Shell"
                                    SettingsSection.SINCRONIZACION -> "Sincronizacion"
                                },
                                color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
                            .padding(horizontal = sidePadding, vertical = topPadding)
                    ) {
                        when (selectedSection) {
                            SettingsSection.PERSONALIZACION -> PersonalizacionContent(isNarrow)
                            SettingsSection.TERMINAL_SHELL -> PlaceholderSection("Terminal y Shell", "Configura tu entorno de terminal y comandos shell.")
                            SettingsSection.SINCRONIZACION -> SincronizacionContent(isNarrow)
                        }
                    }
                }

                if (isNarrow) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showDrawer,
                        enter = slideInHorizontally { -it },
                        exit = slideOutHorizontally { -it }
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f))
                                    .clickable { showDrawer = false }
                            )
                            SettingsSidebar(
                                selectedSection = selectedSection,
                                onSectionClick = { selectedSection = it; showDrawer = false },
                                modifier = Modifier.fillMaxHeight().width(260.dp)
                                    .background(CodeNestColors.surfaceContainerLow)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSidebar(
    selectedSection: SettingsSection,
    onSectionClick: (SettingsSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.15f))
            .padding(vertical = 24.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Configuración",
            color = CodeNestColors.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        SettingsSubNavItem(
            icon = Icons.Default.Palette,
            label = "Personalización",
            isActive = selectedSection == SettingsSection.PERSONALIZACION,
            onClick = { onSectionClick(SettingsSection.PERSONALIZACION) }
        )
        SettingsSubNavItem(
            icon = Icons.Default.Terminal,
            label = "Terminal y Shell",
            isActive = selectedSection == SettingsSection.TERMINAL_SHELL,
            onClick = { onSectionClick(SettingsSection.TERMINAL_SHELL) }
        )
        SettingsSubNavItem(
            icon = Icons.Default.CloudSync,
            label = "Sincronización",
            isActive = selectedSection == SettingsSection.SINCRONIZACION,
            onClick = { onSectionClick(SettingsSection.SINCRONIZACION) }
        )
    }
}

@Composable
private fun SettingsSubNavItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val accentColor = LocalAccentColor.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) accentColor.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = if (isActive) accentColor else CodeNestColors.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Text(label, color = if (isActive) accentColor else CodeNestColors.onSurfaceVariant, fontSize = 14.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun PlaceholderSection(title: String, description: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, color = CodeNestColors.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun SincronizacionContent(isNarrow: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
        ApiSettingsSection()
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = CodeNestColors.outlineVariant.copy(alpha = 0.5f))
        BackupSettingsSection()
    }
}

@Composable
private fun ApiSettingsSection() {
    val aiConfigRepo = koinInject<AiConfigRepository>()
    val savedConfig = remember { aiConfigRepo.loadConfig() }
    var selectedProvider by remember { mutableStateOf(savedConfig.provider) }
    var apiKey by remember { mutableStateOf(savedConfig.apiKey) }
    var customEndpoint by remember { mutableStateOf(savedConfig.customEndpoint) }
    var customModel by remember { mutableStateOf(savedConfig.customModel) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var savedMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }
    var showModelDialog by remember { mutableStateOf(false) }
    var dialogModel by remember { mutableStateOf(customModel.ifBlank { getModelsForProvider(selectedProvider).firstOrNull() ?: "" }) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun resetToSaved() {
        val current = aiConfigRepo.loadConfig()
        selectedProvider = current.provider
        apiKey = current.apiKey
        customEndpoint = current.customEndpoint
        customModel = current.customModel
        savedMessage = null
    }

    fun doSave() {
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                aiConfigRepo.saveConfig(
                    org.example.project.data.network.AiConfig(selectedProvider, apiKey, customEndpoint, dialogModel)
                )
            }
            customModel = dialogModel
            isSuccess = true
            savedMessage = "Configuracion guardada correctamente"
            showModelDialog = false
        }
    }

    if (showModelDialog) {
        AlertDialog(
            onDismissRequest = { showModelDialog = false },
            title = { Text("Confirmar Modelo", fontWeight = FontWeight.Bold, color = CodeNestColors.onSurface) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Proveedor: $selectedProvider", color = CodeNestColors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Selecciona el modelo de IA a utilizar:", color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)

                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(CodeNestColors.surfaceContainerLowest).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(8.dp)).clickable { modelDropdownExpanded = true }.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(dialogModel.ifBlank { "Seleccionar modelo..." }, color = if (dialogModel.isBlank()) CodeNestColors.outlineVariant else CodeNestColors.onSurface, fontSize = 14.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CodeNestColors.onSurfaceVariant)
                        }
                        DropdownMenu(expanded = modelDropdownExpanded, onDismissRequest = { modelDropdownExpanded = false }, modifier = Modifier.background(CodeNestColors.surfaceContainer)) {
                            getModelsForProvider(selectedProvider).forEach { model ->
                                DropdownMenuItem(
                                    text = { Text(model, color = CodeNestColors.onSurface, fontSize = 13.sp) },
                                    onClick = { dialogModel = model; modelDropdownExpanded = false },
                                    modifier = Modifier.background(if (model == dialogModel) CodeNestColors.primary.copy(alpha = 0.1f) else Color.Transparent)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { doSave() },
                    colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.primary, contentColor = CodeNestColors.onPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showModelDialog = false }) { Text("Cancelar", color = CodeNestColors.onSurfaceVariant) }
            },
            containerColor = CodeNestColors.surfaceContainer,
            tonalElevation = 0.dp
        )
    }

    Column {
        Text("Configuración de Entorno", color = CodeNestColors.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
        Text("Gestiona tus credenciales y preferencias de integración del espacio de trabajo.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(bottom = 32.dp))

        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CodeNestColors.surfaceContainer.copy(alpha = 0.6f)).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(12.dp))
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, CodeNestColors.primary.copy(alpha = 0.3f), Color.Transparent))))
            Column(modifier = Modifier.padding(24.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 16.dp)) {
                    Box(modifier = Modifier.size(40.dp).background(CodeNestColors.surfaceContainerHighest, RoundedCornerShape(8.dp)).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Build, contentDescription = null, tint = CodeNestColors.primary) }
                    Column {
                        Text("Configuración de API IA", color = CodeNestColors.onSurface, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text("Esta clave es estrictamente necesaria para habilitar las funciones avanzadas de autocompletado semántico, refactorización y generación de código mediante Inteligencia Artificial dentro de CodeNest.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
                HorizontalDivider(color = CodeNestColors.outlineVariant, modifier = Modifier.padding(vertical = 24.dp))
                Text("Distribuidor", color = CodeNestColors.onSurface, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                val providers = listOf("NVIDIA", "DeepSeek", "Claude", "ChatGPT", "Personalizado")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    providers.forEach { provider ->
                        val isSelected = provider == selectedProvider
                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(if (isSelected) CodeNestColors.primary else Color.Transparent).border(1.dp, if (isSelected) CodeNestColors.primary else CodeNestColors.outlineVariant, RoundedCornerShape(6.dp)).clickable { selectedProvider = provider }.padding(horizontal = 16.dp, vertical = 8.dp), contentAlignment = Alignment.Center) { Text(provider, color = if (isSelected) CodeNestColors.onPrimary else CodeNestColors.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
                    }
                }

                if (selectedProvider == "Personalizado") {
                    Text("Endpoint URL", color = CodeNestColors.onSurface, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = customEndpoint, onValueChange = { customEndpoint = it },
                        placeholder = { Text("https://tu-api.com/v1", color = CodeNestColors.outlineVariant) }, singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CodeNestColors.surfaceContainerLowest, unfocusedContainerColor = CodeNestColors.surfaceContainerLowest, focusedBorderColor = CodeNestColors.primary, unfocusedBorderColor = CodeNestColors.outlineVariant, focusedTextColor = CodeNestColors.onSurface, unfocusedTextColor = CodeNestColors.onSurface),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )
                }
                Text("Modelo", color = CodeNestColors.onSurface, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).clip(RoundedCornerShape(8.dp)).background(CodeNestColors.surfaceContainerLowest).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(8.dp)).clickable { modelDropdownExpanded = selectedProvider != "Personalizado"; if (selectedProvider == "Personalizado") {} else modelDropdownExpanded = !modelDropdownExpanded }.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(customModel.ifBlank { getModelsForProvider(selectedProvider).firstOrNull() ?: "Seleccionar..." }, color = CodeNestColors.onSurface, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CodeNestColors.onSurfaceVariant)
                    }
                    DropdownMenu(expanded = modelDropdownExpanded, onDismissRequest = { modelDropdownExpanded = false }, modifier = Modifier.background(CodeNestColors.surfaceContainer)) {
                        getModelsForProvider(selectedProvider).forEach { model ->
                            DropdownMenuItem(
                                text = { Text(model, color = CodeNestColors.onSurface, fontSize = 13.sp) },
                                onClick = { customModel = model; modelDropdownExpanded = false },
                                modifier = Modifier.background(if (model == customModel) CodeNestColors.primary.copy(alpha = 0.1f) else Color.Transparent)
                            )
                        }
                    }
                }

                Text("Clave API de Proveedor", color = CodeNestColors.onSurface, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(value = apiKey, onValueChange = { apiKey = it }, placeholder = { Text("sk-...", color = CodeNestColors.outlineVariant) }, leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CodeNestColors.outlineVariant) }, trailingIcon = { IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) { Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "Toggle visibility", tint = CodeNestColors.outlineVariant) } }, visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CodeNestColors.surfaceContainerLowest, unfocusedContainerColor = CodeNestColors.surfaceContainerLowest, focusedBorderColor = CodeNestColors.primary, unfocusedBorderColor = CodeNestColors.outlineVariant, focusedTextColor = CodeNestColors.onSurface, unfocusedTextColor = CodeNestColors.onSurface), modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.clickable { }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = CodeNestColors.primary, modifier = Modifier.size(16.dp))
                        Text("¿Cómo obtener una clave de OpenAI o Anthropic?", color = CodeNestColors.primary, fontSize = 13.sp)
                    }
                    Box(modifier = Modifier.background(CodeNestColors.codeNumber.copy(alpha = 0.15f), RoundedCornerShape(4.dp)).border(1.dp, CodeNestColors.codeNumber.copy(alpha = 0.3f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 3.dp)) { Text("Encriptación Local Activa", color = CodeNestColors.codeNumber, fontSize = 11.sp) }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { resetToSaved() },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CodeNestColors.onSurface),
                        modifier = Modifier.padding(end = 16.dp)
                    ) { Text("Cancelar", fontSize = 12.sp) }
                    Button(
                        onClick = {
                            dialogModel = customModel.ifBlank { getModelsForProvider(selectedProvider).firstOrNull() ?: "" }
                            showModelDialog = true
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.primary, contentColor = CodeNestColors.onPrimary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Guardar Configuracion", fontSize = 12.sp)
                        }
                    }
                }
                if (savedMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                            .background(if (isSuccess) CodeNestColors.primary.copy(alpha = 0.1f) else CodeNestColors.error.copy(alpha = 0.1f))
                            .border(1.dp, if (isSuccess) CodeNestColors.primary.copy(alpha = 0.3f) else CodeNestColors.error.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isSuccess) CodeNestColors.primary else CodeNestColors.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(savedMessage!!, color = CodeNestColors.onSurface, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

private fun getModelsForProvider(provider: String): List<String> = when (provider) {
    "NVIDIA" -> listOf(
        "nvidia/llama-3.1-nemotron-70b-instruct",
        "nvidia/llama-3.3-nemotron-super-49b-v1",
        "meta/llama-3.1-405b-instruct",
        "microsoft/phi-3-mini-128k-instruct",
        "google/gemma-2-27b-it",
        "mistralai/mixtral-8x7b-instruct-v0.1"
    )
    "DeepSeek" -> listOf(
        "deepseek-chat",
        "deepseek-v4-pro",
        "deepseek-r1",
        "deepseek-coder"
    )
    "Claude" -> listOf(
        "claude-3-haiku-20240307",
        "claude-3-5-sonnet-20241022",
        "claude-3-opus-20240229",
        "claude-3-sonnet-20240229",
        "claude-3-haiku-20240307"
    )
    "ChatGPT" -> listOf(
        "gpt-4o",
        "gpt-4o-mini",
        "gpt-4-turbo",
        "gpt-4",
        "gpt-3.5-turbo",
        "o1-mini",
        "o1-preview"
    )
    else -> listOf()
}

// ─── PALETA DE COLORES PERSONALIZACIÓN CODENEST ───────────────────────────
private val DarkDeepBlack = Color(0xFF060E20)
private val DarkSurfaceDim = Color(0xFF0B1326)
private val DarkSurfaceContainer = Color(0xFF0F172A)
private val DarkSurfaceContainerHigh = Color(0xFF131B2E)
private val DarkSurfaceContainerHighest = Color(0xFF1E293B)
private val DarkSurfaceVariant = Color(0xFF2D3449)
private val LightDeepBlack = Color(0xFFF0F2F5)
private val LightSurfaceDim = Color(0xFFF5F6FA)
private val LightSurfaceContainer = Color(0xFFFFFFFF)
private val LightSurfaceContainerHigh = Color(0xFFFAFBFD)
private val LightSurfaceContainerHighest = Color(0xFFF0F2F5)
private val LightSurfaceVariant = Color(0xFFE8EAF0)
private val PrimaryNeonCyan = Color(0xFF22D3EE)
private val OnPrimaryDark = Color(0xFF00363E)
private val SecondaryBlue = Color(0xFF81D1F0)
private val TertiaryOrange = Color(0xFFFFD6A3)
private val EmeraldGreen = Color(0xFF10B981)

@Composable private fun surfaceDeepBlack(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightDeepBlack else DarkDeepBlack
@Composable private fun surfaceDim(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightSurfaceDim else DarkSurfaceDim
@Composable private fun surfaceContainer(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightSurfaceContainer else DarkSurfaceContainer
@Composable private fun surfaceContainerHigh(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightSurfaceContainerHigh else DarkSurfaceContainerHigh
@Composable private fun surfaceContainerHighest(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightSurfaceContainerHighest else DarkSurfaceContainerHighest
@Composable private fun surfaceVariant(): Color = if (LocalThemeMode.current == ThemeMode.LIGHT) LightSurfaceVariant else DarkSurfaceVariant

@Composable
private fun PersonalizacionContent(isNarrow: Boolean) {
    Column {
        CustomBreadcrumbs()
        Text(
            text = "Personalización de Interfaz",
            color = CodeNestColors.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(bottom = 120.dp)
        ) {
            SectionPreviewAndBaseTheme(isNarrow)
            SectionAccentColors(isNarrow)
            SectionDetailedSettings(isNarrow)
        }
    }
}

@Composable
private fun CustomBreadcrumbs() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text("Configuración", color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(14.dp))
        Text("Personalización", color = PrimaryNeonCyan, fontSize = 13.sp)
    }
}

@Composable
private fun SectionPreviewAndBaseTheme(isNarrow: Boolean) {
    val currentThemeMode = LocalThemeMode.current
    val setThemeMode = LocalSetThemeMode.current

    if (isNarrow) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text("Tema Base", color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Define la estética base de tu espacio de trabajo.", color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                ThemeCardSelector(modifier = Modifier.weight(1f), label = "Claro", previewBg = Color.White, isSelected = currentThemeMode == ThemeMode.LIGHT, onClick = { setThemeMode(ThemeMode.LIGHT) })
                ThemeCardSelector(modifier = Modifier.weight(1f), label = "Oscuro", previewBg = surfaceDeepBlack(), isSelected = currentThemeMode == ThemeMode.DARK, onClick = { setThemeMode(ThemeMode.DARK) })
                ThemeCardSelector(modifier = Modifier.weight(1f), label = "Sistema", previewBg = surfaceVariant(), isSystemIcon = true, isSelected = currentThemeMode == ThemeMode.SYSTEM, onClick = { setThemeMode(ThemeMode.SYSTEM) })
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            Column(
                modifier = Modifier.weight(1.2f).background(surfaceContainer().copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(surfaceContainer().copy(alpha = 0.3f)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text("VISTA PREVIA GLOBAL", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(surfaceDim()).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.width(48.dp).fillMaxHeight().background(surfaceContainerHigh().copy(alpha = 0.4f), RoundedCornerShape(8.dp)).padding(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(PrimaryNeonCyan.copy(alpha = 0.4f), CircleShape))
                        Box(modifier = Modifier.fillMaxWidth(0.7f).height(4.dp).background(CodeNestColors.onSurfaceVariant.copy(alpha = 0.1f), CircleShape))
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).background(CodeNestColors.onSurfaceVariant.copy(alpha = 0.1f), CircleShape))
                    }
                    Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.width(64.dp).height(12.dp).background(CodeNestColors.onSurface.copy(alpha = 0.1f), RoundedCornerShape(4.dp)))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f).height(48.dp).background(surfaceContainerHigh().copy(alpha = 0.3f), RoundedCornerShape(8.dp)))
                            Box(modifier = Modifier.weight(1f).height(48.dp).background(PrimaryNeonCyan.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).border(1.dp, PrimaryNeonCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp)))
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f).padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Tema Base", color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("Define la estética base de tu espacio de trabajo.", color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    ThemeCardSelector(modifier = Modifier.weight(1f), label = "Claro", previewBg = Color.White, isSelected = currentThemeMode == ThemeMode.LIGHT, onClick = { setThemeMode(ThemeMode.LIGHT) })
                    ThemeCardSelector(modifier = Modifier.weight(1f), label = "Oscuro (Deep)", previewBg = surfaceDeepBlack(), isSelected = currentThemeMode == ThemeMode.DARK, onClick = { setThemeMode(ThemeMode.DARK) })
                    ThemeCardSelector(modifier = Modifier.weight(1f), label = "Sistema", previewBg = surfaceVariant(), isSystemIcon = true, isSelected = currentThemeMode == ThemeMode.SYSTEM, onClick = { setThemeMode(ThemeMode.SYSTEM) })
                }
            }
        }
    }
}

@Composable
private fun ThemeCardSelector(modifier: Modifier = Modifier, label: String, previewBg: Color, isSelected: Boolean = false, isSystemIcon: Boolean = false, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .background(if (isSelected) PrimaryNeonCyan.copy(alpha = 0.05f) else surfaceContainer().copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .border(1.dp, if (isSelected) PrimaryNeonCyan.copy(alpha = 0.4f) else CodeNestColors.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(4f / 3f).background(previewBg, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isSystemIcon) {
                Icon(Icons.Default.SettingsBrightness, contentDescription = null, tint = CodeNestColors.onSurface.copy(alpha = 0.3f))
            }
        }
        Text(
            text = label,
            color = if (isSelected) PrimaryNeonCyan else CodeNestColors.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun SectionAccentColors(isNarrow: Boolean) {
    var selectedColor by remember { mutableStateOf(PrimaryNeonCyan) }
    var selectedColorIndex by remember { mutableStateOf(8) }
    var spectrumPos by remember { mutableStateOf(Offset(0.5f, 0.5f)) }
    var spectrumSize by remember { mutableStateOf(IntSize.Zero) }

    val availableColors = listOf(
        Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFF59E0B), Color(0xFFEAB308),
        Color(0xFF84CC16), Color(0xFF22C55E), Color(0xFF10B981), Color(0xFF14B8A6),
        PrimaryNeonCyan,    Color(0xFF0EA5E3), Color(0xFF3B82F6), Color(0xFF6366F1),
        Color(0xFF8B5CF6), Color(0xFFA855F7), Color(0xFFD946EF), Color(0xFFEC4899)
    )

    val setAccentColor = LocalSetAccentColor.current

    fun selectColor(color: Color) {
        selectedColor = color
        val idx = availableColors.indexOfFirst { it == color }
        selectedColorIndex = if (idx >= 0) idx else -1
        spectrumPos = colorToSpectrumPos(color)
        setAccentColor(color)
    }

    fun pickFromSpectrum2D(pos: Offset) {
        val x = pos.x.coerceIn(0f, 1f)
        val y = pos.y.coerceIn(0f, 1f)
        spectrumPos = Offset(x, y)
        val hueColor = interpolateSpectrumColor(x)
        val brightness = 1f - y
        val color = lerp(Color.Black, hueColor, brightness)
        selectedColor = color
        selectedColorIndex = -1
        setAccentColor(color)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceContainer().copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(32.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Color de Acento", color = CodeNestColors.onSurface, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text("Personaliza botones, enlaces y estados activos del sistema.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier.background(surfaceDeepBlack().copy(alpha = 0.6f), CircleShape).border(1.dp, selectedColor.copy(alpha = 0.3f), CircleShape).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.size(16.dp).background(selectedColor, CircleShape))
                Text(colorToHex(selectedColor), color = selectedColor, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }

        val spectrumPaletteSpacing = if (isNarrow) 24.dp else 48.dp
        if (isNarrow) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spectrumPaletteSpacing)) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("ESPECTRO LIBRE", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    val density = LocalDensity.current
                    Box(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp))
                            .onSizeChanged { spectrumSize = it }
                            .pointerInput(Unit) { detectTapGestures { offset -> pickFromSpectrum2D(Offset(offset.x / spectrumSize.width, offset.y / spectrumSize.height)) } }
                            .pointerInput(Unit) { detectDragGestures { change, _ -> change.consume(); pickFromSpectrum2D(Offset(change.position.x / spectrumSize.width, change.position.y / spectrumSize.height)) } }
                            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    ) {
                        Box(modifier = Modifier.matchParentSize().background(Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red))))
                        Box(modifier = Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))))
                        val cx = with(density) { ((spectrumPos.x * spectrumSize.width).toDp() - 12.dp).coerceIn(0.dp, (spectrumSize.width - 24).toDp().coerceAtLeast(0.dp)) }
                        val cy = with(density) { ((spectrumPos.y * spectrumSize.height).toDp() - 12.dp).coerceIn(0.dp, (spectrumSize.height - 24).toDp().coerceAtLeast(0.dp)) }
                        Box(modifier = Modifier.offset(x = cx, y = cy).size(24.dp).border(2.dp, Color.White, CircleShape))
                    }
                }
                SpectrumPaletteColumn(Modifier.fillMaxWidth(), availableColors, selectedColorIndex, ::selectColor, selectedColor)
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spectrumPaletteSpacing)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("ESPECTRO LIBRE", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    val density = LocalDensity.current
                    Box(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp))
                            .onSizeChanged { spectrumSize = it }
                            .pointerInput(Unit) { detectTapGestures { offset -> pickFromSpectrum2D(Offset(offset.x / spectrumSize.width, offset.y / spectrumSize.height)) } }
                            .pointerInput(Unit) { detectDragGestures { change, _ -> change.consume(); pickFromSpectrum2D(Offset(change.position.x / spectrumSize.width, change.position.y / spectrumSize.height)) } }
                            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    ) {
                        Box(modifier = Modifier.matchParentSize().background(Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red))))
                        Box(modifier = Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))))
                        val cx = with(density) { ((spectrumPos.x * spectrumSize.width).toDp() - 12.dp).coerceIn(0.dp, (spectrumSize.width - 24).toDp().coerceAtLeast(0.dp)) }
                        val cy = with(density) { ((spectrumPos.y * spectrumSize.height).toDp() - 12.dp).coerceIn(0.dp, (spectrumSize.height - 24).toDp().coerceAtLeast(0.dp)) }
                        Box(modifier = Modifier.offset(x = cx, y = cy).size(24.dp).border(2.dp, Color.White, CircleShape))
                    }
                }
                SpectrumPaletteColumn(Modifier.weight(2f), availableColors, selectedColorIndex, ::selectColor, selectedColor)
            }
        }
    }
}

@Composable
private fun SpectrumPaletteColumn(
    modifier: Modifier,
    availableColors: List<Color>,
    selectedColorIndex: Int,
    onSelectColor: (Color) -> Unit,
    selectedColor: Color
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("PALETA SELECCIONADA", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (row in 0 until 2) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 8) {
                        val index = (row * 8) + col
                        val color = availableColors[index]
                        val isSelected = index == selectedColorIndex
                        Box(
                            modifier = Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(color)
                                .border(width = if (isSelected) 2.dp else 0.dp, color = if (isSelected) Color.White else Color.Transparent, shape = RoundedCornerShape(12.dp))
                                .padding(if (isSelected) 4.dp else 0.dp).clickable { onSelectColor(color) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                .background(surfaceContainerHigh().copy(alpha = 0.4f)).border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .clickable { onSelectColor(Color(0xFF6750A4)) }.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Color del Sistema", color = CodeNestColors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("Usa el color de acento predeterminado de tu sistema operativo", color = CodeNestColors.onSurfaceVariant, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(18.dp))
        }

        Row(
            modifier = Modifier.padding(top = 24.dp).fillMaxWidth()
                .background(surfaceDeepBlack().copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .border(1.dp, selectedColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(modifier = Modifier.size(56.dp).background(selectedColor, CircleShape), contentAlignment = Alignment.Center) {
                Text("Aa", color = selectedColor.let { c -> val luminance = 0.299 * c.red + 0.587 * c.green + 0.114 * c.blue; if (luminance > 0.5) Color.Black else Color.White }, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Validación de Accesibilidad", color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Relación de contraste 4.5:1. El color ${colorToHex(selectedColor)} es óptimo para la lectura en el tema oscuro de CodeNest.", color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)
            }
            Icon(Icons.Default.Verified, contentDescription = null, tint = selectedColor, modifier = Modifier.size(28.dp))
        }
    }
}

private fun interpolateSpectrumColor(fraction: Float): Color {
    val stops = listOf(
        Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
    )
    val f = fraction.coerceIn(0f, 1f)
    val segments = stops.size - 1
    val seg = (f * segments).toInt().coerceAtMost(segments - 1)
    val local = (f * segments) - seg
    return lerp(stops[seg], stops[seg + 1], local)
}

private fun colorToSpectrumPos(color: Color): Offset {
    val stops = listOf(
        Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
    )
    var bestX = 0f
    var bestDist = Float.MAX_VALUE
    for (i in 0..100) {
        val f = i / 100f
        val c = interpolateSpectrumColor(f)
        val dr = c.red - color.red
        val dg = c.green - color.green
        val db = c.blue - color.blue
        val dist = dr * dr + dg * dg + db * db
        if (dist < bestDist) {
            bestDist = dist
            bestX = f
        }
    }
    val luminance = 0.299f * color.red + 0.587f * color.green + 0.114f * color.blue
    val bestY = 1f - luminance
    return Offset(bestX, bestY.coerceIn(0f, 1f))
}

private fun colorToHex(color: Color): String {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    return "#%02X%02X%02X".format(r, g, b)
}

@Composable
private fun SectionDetailedSettings(isNarrow: Boolean) {
    if (isNarrow) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            RowCardSettingItem(modifier = Modifier.fillMaxWidth(), icon = Icons.Default.BlurOn, title = "Transparencia Acrílica", description = "Aplica efectos de desenfoque y saturación a paneles y ventanas.", hasToggle = true)
            RowCardSettingItem(modifier = Modifier.fillMaxWidth(), icon = Icons.Default.FormatSize, title = "Escala de UI", description = "Ajusta el tamaño global de los elementos (100%).")
            RowCardSettingItem(modifier = Modifier.fillMaxWidth(), icon = Icons.Default.Animation, title = "Animaciones", description = "Suaviza las transiciones entre paneles (300ms).")
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            RowCardSettingItem(modifier = Modifier.weight(2f), icon = Icons.Default.BlurOn, title = "Transparencia Acrílica", description = "Aplica efectos de desenfoque y saturación a paneles y ventanas.", hasToggle = true)
            RowCardSettingItem(modifier = Modifier.weight(1f), icon = Icons.Default.FormatSize, title = "Escala de UI", description = "Ajusta el tamaño global de los elementos (100%).")
            RowCardSettingItem(modifier = Modifier.weight(1f), icon = Icons.Default.Animation, title = "Animaciones", description = "Suaviza las transiciones entre paneles (300ms).")
        }
    }
}

@Composable
private fun RowCardSettingItem(modifier: Modifier = Modifier, icon: ImageVector, title: String, description: String, hasToggle: Boolean = false) {
    Column(
        modifier = modifier
            .background(surfaceContainer().copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .clickable { }
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryNeonCyan, modifier = Modifier.size(32.dp))
            if (hasToggle) {
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 24.dp)
                        .background(PrimaryNeonCyan.copy(alpha = 0.1f), CircleShape)
                        .border(1.dp, PrimaryNeonCyan.copy(alpha = 0.2f), CircleShape)
                        .padding(4.dp)
                ) {
                    Box(modifier = Modifier.size(16.dp).align(Alignment.CenterEnd).background(PrimaryNeonCyan, CircleShape))
                }
            }
        }
        Text(title, color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun BackupSettingsSection() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(48.dp).background(CodeNestColors.tertiaryContainer.copy(alpha = 0.2f), RoundedCornerShape(12.dp)).border(1.dp, CodeNestColors.tertiary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.CloudSync, contentDescription = null, tint = CodeNestColors.tertiaryContainer, modifier = Modifier.size(24.dp)) }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Configuración de Copia de Seguridad", color = CodeNestColors.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Protege tu repositorio de fragmentos. Al configurar la copia de seguridad, aseguras que tus recetas de código estén seguras y sincronizadas en todos tus entornos de desarrollo.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)
        }
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CodeNestColors.surfaceContainer.copy(alpha = 0.6f)).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(12.dp))) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, CodeNestColors.primary.copy(alpha = 0.5f), Color.Transparent))))
            Row(modifier = Modifier.padding(24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f).padding(end = 24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = CodeNestColors.primary, modifier = Modifier.size(24.dp))
                        Text("Respaldo Automático en la Nube", color = CodeNestColors.onSurface, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Box(modifier = Modifier.background(CodeNestColors.primary.copy(alpha = 0.1f), RoundedCornerShape(99.dp)).border(1.dp, CodeNestColors.primary.copy(alpha = 0.2f), RoundedCornerShape(99.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) { Text("RECOMENDADO", color = CodeNestColors.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sincroniza tus datos de forma segura utilizando encriptación AES-256. Tus snippets estarán disponibles incluso si cambias de dispositivo.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(CodeNestColors.secondary, CircleShape))
                            Text("Estado: Inactivo", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                        Text("Ult. Backup: --", color = CodeNestColors.onSurfaceVariant, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
                Button(onClick = { }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.primary, contentColor = CodeNestColors.onPrimary), contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Guardar en la nube", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
            OutlinedButton(onClick = { }, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CodeNestColors.onSurfaceVariant), border = BorderStroke(1.dp, CodeNestColors.outlineVariant)) { Text("En otro momento", fontSize = 14.sp) }
        }
        HorizontalDivider(color = CodeNestColors.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(top = 32.dp, bottom = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Los datos se encriptan localmente antes de la transmisión.", color = CodeNestColors.outline, fontSize = 12.sp)
            Row(modifier = Modifier.clickable { }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Ver política de privacidad", color = CodeNestColors.primary, fontSize = 12.sp)
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = CodeNestColors.primary, modifier = Modifier.size(14.dp))
            }
        }
    }
}
