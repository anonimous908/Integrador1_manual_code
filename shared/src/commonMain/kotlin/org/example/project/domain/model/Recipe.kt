package org.example.project.domain.model

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

