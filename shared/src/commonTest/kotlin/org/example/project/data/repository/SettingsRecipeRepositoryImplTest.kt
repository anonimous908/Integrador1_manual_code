package org.example.project.data.repository

import com.russhwolf.settings.Settings

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.example.project.domain.model.Recipe

/**
 * In-memory [Settings] implementation backed by a [MutableMap].
 * Replaces [MockSettings] which is not available on all KMP targets.
 */
class InMemorySettings : Settings {
    private val map = mutableMapOf<String, String>()

    override fun getString(key: String, defaultValue: String): String = map[key] ?: defaultValue
    override fun getStringOrNull(key: String): String? = map[key]
    override fun putString(key: String, value: String) { map[key] = value }
    override fun remove(key: String) { map.remove(key) }
    override fun clear() { map.clear() }
    override fun getInt(key: String, defaultValue: Int): Int =
        map[key]?.toIntOrNull() ?: defaultValue
    override fun getIntOrNull(key: String): Int? = map[key]?.toIntOrNull()
    override fun putInt(key: String, value: Int) { map[key] = value.toString() }
    override fun getLong(key: String, defaultValue: Long): Long =
        map[key]?.toLongOrNull() ?: defaultValue
    override fun getLongOrNull(key: String): Long? = map[key]?.toLongOrNull()
    override fun putLong(key: String, value: Long) { map[key] = value.toString() }
    override fun getFloat(key: String, defaultValue: Float): Float =
        map[key]?.toFloatOrNull() ?: defaultValue
    override fun getFloatOrNull(key: String): Float? = map[key]?.toFloatOrNull()
    override fun putFloat(key: String, value: Float) { map[key] = value.toString() }
    override fun getDouble(key: String, defaultValue: Double): Double =
        map[key]?.toDoubleOrNull() ?: defaultValue
    override fun getDoubleOrNull(key: String): Double? = map[key]?.toDoubleOrNull()
    override fun putDouble(key: String, value: Double) { map[key] = value.toString() }
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        map[key]?.toBooleanStrictOrNull() ?: defaultValue
    override fun getBooleanOrNull(key: String): Boolean? = map[key]?.toBooleanStrictOrNull()
    override fun putBoolean(key: String, value: Boolean) { map[key] = value.toString() }
    override val keys: Set<String> get() = map.keys.toSet()
    override val size: Int get() = map.size
    override fun hasKey(key: String): Boolean = map.containsKey(key)
}

class SettingsRecipeRepositoryImplTest {

    private fun createRepo(): SettingsRecipeRepositoryImpl {
        val settings = InMemorySettings()
        return SettingsRecipeRepositoryImpl(settings)
    }

    // ─── Create ────────────────────────────────────────────────────────

    @Test
    fun `create persists a new recipe and can be retrieved`() = runTest {
        val repo = createRepo()
        val recipe = Recipe.empty(userId = "user@ex.com")

        val saved = repo.create(recipe)

        assertEquals(recipe.id, saved.id)
        assertEquals(recipe.userId, saved.userId)

        val retrieved = repo.getById(recipe.id)
        assertNotNull(retrieved)
        assertEquals(recipe.id, retrieved.id)
    }

    // ─── Get By ID ─────────────────────────────────────────────────────

    @Test
    fun `getById returns matching recipe`() = runTest {
        val repo = createRepo()
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe)

        val result = repo.getById(recipe.id)

        assertNotNull(result)
        assertEquals(recipe.id, result.id)
        assertEquals(recipe.userId, result.userId)
    }

    @Test
    fun `getById returns null for missing recipe`() = runTest {
        val repo = createRepo()

        val result = repo.getById("non-existent-id")

        assertNull(result)
    }

    // ─── Get Personal Recipes ──────────────────────────────────────────

    @Test
    fun `getPersonalRecipes returns all created recipes`() = runTest {
        val repo = createRepo()
        val recipe1 = Recipe.empty(userId = "user@ex.com")
        val recipe2 = Recipe.empty(userId = "user@ex.com")
        val recipe3 = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe1)
        repo.create(recipe2)
        repo.create(recipe3)

        val recipes = repo.getPersonalRecipes()

        assertEquals(3, recipes.size)
        assertTrue(recipes.any { it.id == recipe1.id })
        assertTrue(recipes.any { it.id == recipe2.id })
        assertTrue(recipes.any { it.id == recipe3.id })
    }

    @Test
    fun `getPersonalRecipes returns empty list when no recipes`() = runTest {
        val repo = createRepo()

        val recipes = repo.getPersonalRecipes()

        assertTrue(recipes.isEmpty())
    }

    // ─── Update ────────────────────────────────────────────────────────

    @Test
    fun `update overwrites existing recipe fields`() = runTest {
        val repo = createRepo()
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe)

        val updated = repo.update(recipe.copy(title = "New Title"))

        assertEquals("New Title", updated.title)

        val retrieved = repo.getById(recipe.id)
        assertNotNull(retrieved)
        assertEquals("New Title", retrieved.title)
    }

    // ─── Delete ────────────────────────────────────────────────────────

    @Test
    fun `delete removes recipe and returns true`() = runTest {
        val repo = createRepo()
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo.create(recipe)

        val deleted = repo.delete(recipe.id)

        assertTrue(deleted)
        assertNull(repo.getById(recipe.id))
    }

    @Test
    fun `delete returns false for non-existent recipe`() = runTest {
        val repo = createRepo()

        val deleted = repo.delete("non-existent-id")

        assertFalse(deleted)
    }

    // ─── Persistence across instances ──────────────────────────────────

    @Test
    fun `recipes persist across Settings instances`() = runTest {
        val settings = InMemorySettings()
        val repo1 = SettingsRecipeRepositoryImpl(settings)
        val recipe = Recipe.empty(userId = "user@ex.com")
        repo1.create(recipe)

        // New instance with same Settings — data persisted
        val repo2 = SettingsRecipeRepositoryImpl(settings)
        val recipes = repo2.getPersonalRecipes()

        assertEquals(1, recipes.size)
        assertEquals(recipe.id, recipes.first().id)
    }
}
