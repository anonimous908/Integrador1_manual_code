package org.example.project.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.theme.DevSpaceColors

private val monoFont = FontFamily.Monospace

data class SnippetCardData(
    val title: String,
    val languageTag: String,
    val secondaryTag: String,
    val iconAccent: Color,
    val codeLines: List<String>,
    val footerLabel: String,
    val footerIcon: ImageVector,
    val activeLineIndex: Int = -1,
    val showTerminalOutput: Boolean = false,
    val terminalLines: List<String> = emptyList()
)

val sampleCards = listOf(
    SnippetCardData(
        title = "Grupo A1",
        languageTag = "React",
        secondaryTag = "UI",
        iconAccent = DevSpaceColors.primary,
        activeLineIndex = 3,
        footerLabel = "Evidence",
        footerIcon = Icons.Filled.Visibility,
        codeLines = listOf(
            "import { motion } from 'framer-motion';",
            "",
            "export const Tabs = ({ items }) => {",
            "  return (",
            "    <div className=\"flex space-x-1 rounded-xl bg-blue-900/20 p-1\">",
            "      {items.map((item) => (",
            "        <motion.button layoutId=\"activeTab\">",
            "          {item.label}",
            "        </motion.button>",
            "      ))}",
            "    </div>",
            "  );",
            "};"
        )
    ),
    SnippetCardData(
        title = "Grupo A2",
        languageTag = "Python",
        secondaryTag = "Data",
        iconAccent = DevSpaceColors.secondary,
        activeLineIndex = 5,
        footerLabel = "Output Log",
        footerIcon = Icons.Filled.Visibility,
        showTerminalOutput = true,
        terminalLines = listOf(
            "> python clean.py",
            "[INFO] Loading dataset...",
            "[INFO] Initial shape: (10500, 42)",
            "[INFO] Dropping sparse rows...",
            "[INFO] Final shape: (9820, 42)",
            "[SUCCESS] Data cleaned in 0.4s"
        ),
        codeLines = listOf(
            "import pandas as pd",
            "import numpy as np",
            "",
            "def clean_dataset(df):",
            "    \"\"\"Removes nulls and normalizes text columns\"\"\"",
            "    df = df.dropna(thresh=int(0.8 * len(df.columns)))",
            "",
            "    for col in df.select_dtypes(include=['object']):",
            "        df[col] = df[col].str.lower().str.strip()",
            "",
            "    return df",
            "",
            "cleaned_data = clean_dataset(raw_data)"
        )
    ),
    SnippetCardData(
        title = "Grupo A3",
        languageTag = "SQL",
        secondaryTag = "Analytics",
        iconAccent = DevSpaceColors.tertiary,
        activeLineIndex = 3,
        footerLabel = "Visualization",
        footerIcon = Icons.Filled.Visibility,
        codeLines = listOf(
            "WITH cohort_items AS (",
            "  SELECT",
            "    user_id,",
            "    DATE_TRUNC('month', MIN(created_at)) AS cohort_month",
            "  FROM subscriptions",
            "  GROUP BY 1",
            ")",
            "SELECT",
            "  c.cohort_month,",
            "  COUNT(DISTINCT s.user_id) AS active_users",
            "FROM cohort_items c",
            "LEFT JOIN subscriptions s ON c.user_id = s.user_id"
        )
    )
)

private val SQL_KEYWORDS = setOf(
    "WITH", "AS", "SELECT", "FROM", "GROUP", "BY", "LEFT", "JOIN", "ON",
    "DISTINCT", "WHERE", "ORDER"
)
private val GENERIC_KEYWORDS = setOf(
    "import", "from", "export", "const", "return", "def", "for", "in",
    "class", "if", "else", "while", "fun", "val", "var"
)

private fun highlightLine(line: String): AnnotatedString = buildAnnotatedString {
    if (line.isBlank()) {
        append(line)
        return@buildAnnotatedString
    }

    val trimmed = line.trimStart()
    if (trimmed.startsWith("#") || trimmed.startsWith("--") ||
        (trimmed.startsWith("\"\"\"") && trimmed.endsWith("\"\"\"") && trimmed.length > 3)
    ) {
        withStyle(SpanStyle(color = DevSpaceColors.codeComment)) { append(line) }
        return@buildAnnotatedString
    }

    val tokenRegex = Regex(
        "(\"[^\"]*\"|'[^']*')" + // strings
            "|\\b\\d+(\\.\\d+)?\\b" + // numbers
            "|\\b[A-Za-z_][A-Za-z0-9_]*\\b" // identifiers / keywords
    )

    var lastIndex = 0
    for (match in tokenRegex.findAll(line)) {
        if (match.range.first > lastIndex) {
            append(line.substring(lastIndex, match.range.first))
        }
        val token = match.value
        val style = when {
            token.startsWith("\"") || token.startsWith("'") -> SpanStyle(color = DevSpaceColors.codeString)
            token.firstOrNull()?.isDigit() == true -> SpanStyle(color = DevSpaceColors.codeNumber)
            token.uppercase() in SQL_KEYWORDS || token in GENERIC_KEYWORDS ->
                SpanStyle(color = DevSpaceColors.codeKeyword)
            token.firstOrNull()?.isUpperCase() == true -> SpanStyle(color = DevSpaceColors.codeType)
            else -> SpanStyle(color = DevSpaceColors.onSurfaceVariant)
        }
        withStyle(style) { append(token) }
        lastIndex = match.range.last + 1
    }
    if (lastIndex < line.length) {
        append(line.substring(lastIndex))
    }
}

@Composable
fun SnippetCard(
    card: SnippetCardData,
    onCopied: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DevSpaceColors.surfaceContainer.copy(alpha = 0.6f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
    ) {
        // Header de la card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DevSpaceColors.surfaceContainerLow.copy(alpha = 0.5f))
                .border(width = 0.5.dp, color = DevSpaceColors.outlineVariant.copy(alpha = 0.5f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Folder, contentDescription = null, tint = card.iconAccent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(card.title, color = DevSpaceColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Tag(text = card.languageTag, accent = card.iconAccent)
                Spacer(modifier = Modifier.width(4.dp))
                Tag(text = card.secondaryTag, accent = DevSpaceColors.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            val code = card.codeLines.joinToString("\n")
                            clipboardManager.setText(AnnotatedString(code))
                            onCopied()
                        }
                        .padding(2.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = DevSpaceColors.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Bloque de código
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DevSpaceColors.codeBg)
                .padding(8.dp)
        ) {
            card.codeLines.forEachIndexed { index, line ->
                val isActive = index == card.activeLineIndex
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isActive) DevSpaceColors.primary.copy(alpha = 0.05f) else Color.Transparent)
                ) {
                    if (isActive) {
                        Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(DevSpaceColors.primary))
                    } else {
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    Text(
                        text = "${index + 1}",
                        color = DevSpaceColors.tertiaryContainer.copy(alpha = 0.5f),
                        fontFamily = monoFont,
                        fontSize = 13.sp,
                        modifier = Modifier.width(28.dp).padding(end = 8.dp),
                    )
                    Text(
                        text = highlightLine(line),
                        fontFamily = monoFont,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // Footer: evidencia visual o log de terminal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(DevSpaceColors.surfaceContainerLowest)
                .border(width = 0.5.dp, color = DevSpaceColors.outlineVariant.copy(alpha = 0.5f))
        ) {
            if (card.showTerminalOutput) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DevSpaceColors.surfaceContainerLowest)
                        .padding(8.dp)
                ) {
                    card.terminalLines.forEach { line ->
                        Text(
                            line,
                            color = DevSpaceColors.secondary.copy(alpha = 0.7f),
                            fontFamily = monoFont,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            // Degradado inferior con etiqueta + botón compartir
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, DevSpaceColors.background.copy(alpha = 0.85f))
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(card.footerIcon, contentDescription = null, tint = DevSpaceColors.onSurface, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(card.footerLabel, color = DevSpaceColors.onSurface, fontSize = 11.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            val code = card.codeLines.joinToString("\n")
                            clipboardManager.setText(AnnotatedString(code))
                            onCopied()
                        }
                        .padding(2.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = DevSpaceColors.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun Tag(text: String, accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(accent.copy(alpha = 0.15f))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, color = accent, fontSize = 10.sp)
    }
}
