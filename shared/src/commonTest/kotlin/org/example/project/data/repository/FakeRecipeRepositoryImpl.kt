package org.example.project.data.repository

import org.example.project.domain.model.Recipe
import org.example.project.domain.repository.RecipeRepository

/**
 * In-memory [RecipeRepository] implementation backed by a [MutableMap].
 * Intended for unit tests — no I/O or settings dependency.
 */
class FakeRecipeRepositoryImpl : RecipeRepository {

    private val recipes = mutableMapOf<String, Recipe>()

    override suspend fun getPersonalRecipes(): List<Recipe> {
        return recipes.values.toList()
    }

    override suspend fun getById(id: String): Recipe? {
        return recipes[id]
    }

    override suspend fun create(recipe: Recipe): Recipe {
        recipes[recipe.id] = recipe
        return recipe
    }

    override suspend fun update(recipe: Recipe): Recipe {
        recipes[recipe.id] = recipe
        return recipe
    }

    override suspend fun delete(id: String): Boolean {
        return recipes.remove(id) != null
    }
}
