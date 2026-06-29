package org.example.project.domain.repository

import org.example.project.domain.model.SnippetCardData

interface RecipeRepository {
    suspend fun getPersonalRecipes(): List<SnippetCardData>
}
