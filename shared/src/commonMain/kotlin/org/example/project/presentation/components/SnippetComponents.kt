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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.SnippetCardData
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.presentation.theme.DevSpaceColors

private val monoFont = FontFamily.Monospace

@Composable
fun SnippetCard(
    card: SnippetCardData,
    syntaxHighlighter: SyntaxHighlighter,
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
                        text = syntaxHighlighter.highlight(line),
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
