package org.example.project.data.repository

import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.domain.model.Recipe
import org.example.project.domain.repository.RecipeRepository

/**
 * [RecipeRepository] implementation backed by [multiplatform-settings][Settings].
 *
 * All recipes are stored as a single JSON array under the `recipe_all` key,
 * prefixed with `recipe_` for key isolation.
 */
class SettingsRecipeRepositoryImpl(
    private val settings: Settings
) : RecipeRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val recipeKey = "recipe_all"

    override suspend fun getPersonalRecipes(): List<Recipe> {
        return try {
            val stored = settings.getStringOrNull(recipeKey)
            if (stored.isNullOrBlank()) {
                emptyList()
            } else {
                json.decodeFromString<List<Recipe>>(stored)
            }
        } catch (e: Exception) {
            // Log error and return empty on corruption
            emptyList()
        }
    }

    override suspend fun getById(id: String): Recipe? {
        return getPersonalRecipes().find { it.id == id }
    }

    override suspend fun create(recipe: Recipe): Recipe {
        val recipes = getPersonalRecipes().toMutableList()
        recipes.add(recipe)
        saveRecipes(recipes)
        return recipe
    }

    override suspend fun update(recipe: Recipe): Recipe {
        val recipes = getPersonalRecipes().toMutableList()
        val index = recipes.indexOfFirst { it.id == recipe.id }
        if (index != -1) {
            recipes[index] = recipe
            saveRecipes(recipes)
        }
        return recipe
    }

    override suspend fun delete(id: String): Boolean {
        val recipes = getPersonalRecipes().toMutableList()
        val removed = recipes.removeAll { it.id == id }
        if (removed) {
            saveRecipes(recipes)
        }
        return removed
    }

    private fun saveRecipes(recipes: List<Recipe>) {
        try {
            settings.putString(recipeKey, json.encodeToString(recipes))
        } catch (e: Exception) {
            // Log error on save failure
        }
    }
}
