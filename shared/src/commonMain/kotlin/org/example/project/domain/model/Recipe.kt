package org.example.project.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/**
 * Unified domain model for a code recipe.
 * Replaces the fragmented SnippetCardData + SnippetDetail types.
 */
@Serializable
data class Recipe(
    val id: String,
    val userId: String,
    val title: String = "",
    val languageTag: String = "",
    val secondaryTag: String? = null,
    val code: List<String> = emptyList(),
    val description: String = "",
    val dependencies: List<String> = emptyList(),
    val evidence: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    companion object {
        /**
         * Creates a new Recipe with a generated UUID, the given userId,
         * and all other fields at their defaults.
         *
         * Timestamps default to 0L and SHOULD be set by the repository
         * during create/update.
         */
        fun empty(userId: String): Recipe = Recipe(
            id = Uuid.random().toString(),
            userId = userId
        )
    }
}

/**
 * Adapter: maps a [SnippetCardData] (legacy UI model) to a [Recipe].
 * Generates a fresh ID if none exists. The [userId] parameter identifies
 * the recipe owner.
 */
fun SnippetCardData.toRecipe(userId: String = ""): Recipe = Recipe(
    id = Uuid.random().toString(),
    userId = userId,
    title = title,
    languageTag = languageTag,
    secondaryTag = if (secondaryTag.isBlank()) null else secondaryTag,
    code = codeLines
)

/**
 * Adapter: maps a [Recipe] back to [SnippetCardData] (legacy UI model).
 * Compose-specific UI fields receive sensible defaults.
 */
fun Recipe.toSnippetCardData(): SnippetCardData = SnippetCardData(
    title = title,
    languageTag = languageTag,
    secondaryTag = secondaryTag ?: "",
    iconAccent = Color(0xFF6C63FF),
    codeLines = code,
    footerLabel = if (evidence.isNotBlank()) "Evidence" else "Details",
    footerIcon = Icons.Filled.Visibility,
    activeLineIndex = -1,
    showTerminalOutput = false,
    terminalLines = emptyList()
)
