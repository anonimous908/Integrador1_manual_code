package org.example.project.domain.repository

import org.example.project.domain.model.Recipe

interface RecipeRepository {
    suspend fun getPersonalRecipes(): List<Recipe>
    suspend fun getById(id: String): Recipe?
    suspend fun create(recipe: Recipe): Recipe
    suspend fun update(recipe: Recipe): Recipe
    suspend fun delete(id: String): Boolean
}
