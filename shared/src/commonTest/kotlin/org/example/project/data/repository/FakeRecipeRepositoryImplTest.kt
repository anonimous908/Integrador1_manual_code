package org.example.project.data.repository

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.example.project.domain.model.Recipe

class FakeRecipeRepositoryImplTest {

    @Test
    fun `create stores recipe and getById returns it`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "user@ex.com")

        repo.create(recipe)
        val retrieved = repo.getById(recipe.id)

        assertNotNull(retrieved)
        assertEquals(recipe.id, retrieved.id)
        assertEquals(recipe.userId, retrieved.userId)
    }

    @Test
    fun `getById returns null for missing id`() = runTest {
        val repo = FakeRecipeRepositoryImpl()

        val result = repo.getById("non-existent")

        assertNull(result)
    }

    @Test
    fun `getPersonalRecipes returns all stored recipes`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val r1 = Recipe.empty(userId = "u1")
        val r2 = Recipe.empty(userId = "u2")
        repo.create(r1)
        repo.create(r2)

        val all = repo.getPersonalRecipes()

        assertEquals(2, all.size)
    }

    @Test
    fun `getPersonalRecipes returns empty when none stored`() = runTest {
        val repo = FakeRecipeRepositoryImpl()

        assertTrue(repo.getPersonalRecipes().isEmpty())
    }

    @Test
    fun `update modifies existing recipe`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe)

        repo.update(recipe.copy(title = "Updated"))

        val retrieved = repo.getById(recipe.id)
        assertNotNull(retrieved)
        assertEquals("Updated", retrieved.title)
    }

    @Test
    fun `delete removes recipe and returns true`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe)

        val deleted = repo.delete(recipe.id)

        assertTrue(deleted)
        assertNull(repo.getById(recipe.id))
    }

    @Test
    fun `delete returns false for missing recipe`() = runTest {
        val repo = FakeRecipeRepositoryImpl()

        assertFalse(repo.delete("non-existent"))
    }

    @Test
    fun `full CRUD sequence behaves consistently`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "user@ex.com")

        // Create
        repo.create(recipe)
        assertEquals(1, repo.getPersonalRecipes().size)

        // Read
        assertNotNull(repo.getById(recipe.id))

        // Update
        repo.update(recipe.copy(title = "Changed"))
        assertEquals("Changed", repo.getById(recipe.id)?.title)

        // Delete
        assertTrue(repo.delete(recipe.id))
        assertEquals(0, repo.getPersonalRecipes().size)
    }
}
