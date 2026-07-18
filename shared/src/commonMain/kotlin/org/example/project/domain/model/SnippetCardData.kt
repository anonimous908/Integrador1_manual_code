package org.example.project.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Legacy UI model for displaying a snippet card in the grid view.
 * @deprecated Use [Recipe] instead. Adapter functions [SnippetCardData.toRecipe]
 * and [Recipe.toSnippetCardData] are available for migration.
 */
@Deprecated("Use Recipe instead. Migrate via SnippetCardData.toRecipe() / Recipe.toSnippetCardData()")
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
