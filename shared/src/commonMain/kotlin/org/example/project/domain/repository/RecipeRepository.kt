package org.example.project.domain.repository

import org.example.project.domain.model.SnippetCardData
import org.example.project.presentation.SnippetDetail

interface RecipeRepository {
    suspend fun getPersonalRecipes(): List<SnippetCardData>
    suspend fun saveRecipe(snippet: SnippetDetail)
    suspend fun deleteRecipe(title: String)
    suspend fun getAllSnippets(): List<SnippetDetail>
}
