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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.buildAnnotatedString
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
import org.example.project.domain.model.Recipe
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.presentation.theme.CodeNestColors

private val monoFont = FontFamily.Monospace

@Composable
fun SnippetCard(
    recipe: Recipe,
    syntaxHighlighter: SyntaxHighlighter,
    onCopied: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val fullCode = remember(recipe) { recipe.code.joinToString("\n") }
    val highlightedLines = remember(recipe, fullCode) {
        syntaxHighlighter.highlight(fullCode, recipe.languageTag).splitLines()
    }
    val iconAccent = remember(recipe.languageTag) {
        when (recipe.languageTag.lowercase()) {
            "kotlin" -> Color(0xFF6C63FF)
            "python" -> Color(0xFF4CAF50)
            "javascript", "js" -> Color(0xFFF1C40F)
            else -> Color(0xFF3498DB)
        }
    }
    val footerLabel = if (recipe.evidence.isNotBlank()) "Evidence" else "Details"

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        color = CodeNestColors.surfaceContainer.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CodeNestColors.surfaceContainerLow.copy(alpha = 0.5f))
                    .border(width = 0.5.dp, color = CodeNestColors.outlineVariant.copy(alpha = 0.5f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Folder, contentDescription = null, tint = iconAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(recipe.title, color = CodeNestColors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CodeNestTagChip(text = recipe.languageTag, accent = iconAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    if (!recipe.secondaryTag.isNullOrBlank()) {
                        CodeNestTagChip(text = recipe.secondaryTag, accent = CodeNestColors.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                clipboardManager.setText(AnnotatedString(fullCode))
                                onCopied()
                            }
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CodeNestColors.codeBg)
                    .padding(8.dp)
            ) {
                recipe.code.forEachIndexed { index, line ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${index + 1}",
                            color = CodeNestColors.tertiaryContainer.copy(alpha = 0.5f),
                            fontFamily = monoFont,
                            fontSize = 13.sp,
                            modifier = Modifier.width(28.dp).padding(end = 8.dp),
                        )
                        Text(
                            text = highlightedLines.getOrElse(index) { AnnotatedString(line) },
                            color = CodeNestColors.onSurfaceVariant,
                            fontFamily = monoFont,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(CodeNestColors.surfaceContainerLowest)
                    .border(width = 0.5.dp, color = CodeNestColors.outlineVariant.copy(alpha = 0.5f))
            ) {
                if (recipe.evidence.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CodeNestColors.surfaceContainerLowest)
                            .padding(8.dp)
                    ) {
                        recipe.evidence.split("\n").forEach { line ->
                            Text(
                                line,
                                color = CodeNestColors.secondary.copy(alpha = 0.7f),
                                fontFamily = monoFont,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, CodeNestColors.background.copy(alpha = 0.85f))
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
                        Icon(Icons.Filled.Visibility, contentDescription = null, tint = CodeNestColors.onSurface, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(footerLabel, color = CodeNestColors.onSurface, fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                clipboardManager.setText(AnnotatedString(fullCode))
                                onCopied()
                            }
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = CodeNestColors.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}


private fun AnnotatedString.splitLines(): List<AnnotatedString> {
    val lines = text.split('\n')
    var currentOffset = 0
    return lines.map { lineText ->
        val lineStart = currentOffset
        val lineEnd = currentOffset + lineText.length
        currentOffset = lineEnd + 1 // +1 for the newline character

        buildAnnotatedString {
            append(lineText)
            spanStyles.forEach { range ->
                val spanStart = range.start
                val spanEnd = range.end
                val overlapStart = maxOf(spanStart, lineStart)
                val overlapEnd = minOf(spanEnd, lineEnd)
                if (overlapStart < overlapEnd) {
                    addStyle(
                        style = range.item,
                        start = overlapStart - lineStart,
                        end = overlapEnd - lineStart
                    )
                }
            }
        }
    }
}

@Composable
fun SnippetListItem(
    recipe: Recipe,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = recipe.title,
                color = CodeNestColors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                CodeNestTagChip(
                    text = recipe.languageTag,
                    accent = CodeNestColors.tertiary,
                    shapeRadius = 4.dp
                )
                if (!recipe.secondaryTag.isNullOrBlank()) {
                    CodeNestTagChip(
                        text = recipe.secondaryTag,
                        accent = CodeNestColors.onSurfaceVariant,
                        shapeRadius = 4.dp
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = CodeNestColors.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

