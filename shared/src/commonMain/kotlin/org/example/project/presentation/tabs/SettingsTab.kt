package org.example.project.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.theme.ThemeMode
import androidx.compose.ui.graphics.vector.rememberVectorPainter

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember {
                TabOptions(index = 3u, title = "Configuración", icon = icon)
            }
        }

    @Composable
    override fun Content() {
        PersonalizacionContent()
    }
}

@Composable
private fun PersonalizacionContent() {
    var currentThemeMode by remember { mutableStateOf(ThemeMode.DARK) }

    // Sync reactive colors
    fun refreshColors() {
        currentThemeMode = currentThemeMode // trigger recomposition
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodeNestColors.background)
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Personalización",
            color = CodeNestColors.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Personaliza los colores de la interfaz. Los cambios se aplican en vivo.",
            color = CodeNestColors.onSurfaceVariant,
            fontSize = 14.sp
        )

        // ─── Theme Mode Selector ──────────────────────────────────
        SectionCard(title = "Modo de Tema") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(ThemeMode.DEFAULT to "Original", ThemeMode.DARK to "Oscuro", ThemeMode.LIGHT to "Claro").forEach { (mode, label) ->
                    FilterChip(
                        selected = currentThemeMode == mode,
                        onClick = {
                            currentThemeMode = mode
                            CodeNestColors.updateTheme(mode == ThemeMode.DARK)
                        },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CodeNestColors.primary.copy(alpha = 0.15f),
                            selectedLabelColor = CodeNestColors.primary
                        )
                    )
                }
            }
        }

        // Hide color pickers when DEFAULT
        if (currentThemeMode != ThemeMode.DEFAULT) {
            SectionAccentColor()
            SectionAdditionalColors()
            SectionSurfaceColor()

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    CodeNestColors.resetToDefaults()
                    refreshColors()
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CodeNestColors.error,
                    contentColor = CodeNestColors.onError
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Restaurar colores originales", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Los colores personalizados están ocultos en modo Original.",
                    color = CodeNestColors.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ─── Section Components ────────────────────────────────────────────

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeNestColors.surfaceContainer.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(title, color = CodeNestColors.onSurface, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun SectionAccentColor() {
    var selectedColor by remember { mutableStateOf(CodeNestColors.primary) }

    SectionCard(title = "Color de Acento") {
        Text("Define el color principal de la interfaz.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)

        // Preview
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(CodeNestColors.surface.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(selectedColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aa",
                    color = if (selectedColor.luminance() > 0.5) Color.Black else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text("Vista previa", color = CodeNestColors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(colorToHex(selectedColor), color = CodeNestColors.onSurfaceVariant, fontSize = 12.sp)
            }
        }

        ColorPickerRow(
            label = "Color Primario",
            color = selectedColor,
            onColorChange = {
                selectedColor = it
                CodeNestColors.updateAccent(it)
            }
        )
    }
}

@Composable
private fun SectionAdditionalColors() {
    var selSecondary by remember { mutableStateOf(CodeNestColors.secondary) }
    var selTertiary by remember { mutableStateOf(CodeNestColors.tertiary) }
    var selError by remember { mutableStateOf(CodeNestColors.error) }

    SectionCard(title = "Colores Adicionales") {
        Text("Personaliza color secundario, terciario y de error.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)

        ColorPickerRow("Color Secundario", selSecondary) { selSecondary = it; CodeNestColors.updateSecondary(it) }
        ColorPickerRow("Color Terciario", selTertiary) { selTertiary = it; CodeNestColors.updateTertiary(it) }
        ColorPickerRow("Color de Error", selError) { selError = it; CodeNestColors.updateError(it) }
    }
}

@Composable
private fun SectionSurfaceColor() {
    var selSurface by remember { mutableStateOf(CodeNestColors.surface) }

    SectionCard(title = "Color de Superficie") {
        Text("Cambia el fondo general y contenedores.", color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)

        ColorPickerRow("Superficie", selSurface) { selSurface = it; CodeNestColors.updateSurface(it) }
    }
}

// ─── Color Picker ──────────────────────────────────────────────────

@Composable
private fun ColorPickerRow(
    label: String,
    color: Color,
    onColorChange: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentColor by remember(color) { mutableStateOf(color) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .background(CodeNestColors.surfaceContainerLow.copy(alpha = 0.5f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(24.dp).background(currentColor, RoundedCornerShape(4.dp)).border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(4.dp)))
                Text(label, color = CodeNestColors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, tint = CodeNestColors.onSurfaceVariant)
        }

        if (expanded) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                val presets = listOf(
                    Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFF59E0B), Color(0xFF84CC16),
                    Color(0xFF22C55E), Color(0xFF14B8A6), Color(0xFF3B82F6), Color(0xFF8B5CF6),
                    Color(0xFFD946EF), Color(0xFFEC4899), Color(0xFF78716C), Color(0xFF1C1917)
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    presets.take(6).forEach { c ->
                        Box(Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(c).border(
                            if (currentColor == c) 2.dp else 0.dp,
                            if (currentColor == c) CodeNestColors.primary else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        ).clickable { currentColor = c; onColorChange(c) })
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    presets.drop(6).forEach { c ->
                        Box(Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(c).border(
                            if (currentColor == c) 2.dp else 0.dp,
                            if (currentColor == c) CodeNestColors.primary else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        ).clickable { currentColor = c; onColorChange(c) })
                    }
                }
            }
        }
    }
}

private fun colorToHex(color: Color): String {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    return "#%02X%02X%02X".format(r, g, b)
}

private fun Color.luminance(): Double = 0.299 * red + 0.587 * green + 0.114 * blue
