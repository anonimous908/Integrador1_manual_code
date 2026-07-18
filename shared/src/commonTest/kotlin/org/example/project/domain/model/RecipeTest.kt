package org.example.project.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertNull

class RecipeTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    // ─── Serialization ─────────────────────────────────────────────────

    @Test
    fun `serialization roundtrip preserves all fields`() {
        val recipe = Recipe(
            id = "abc-123",
            userId = "user@ex.com",
            title = "My Recipe",
            languageTag = "Python",
            secondaryTag = "Data",
            code = listOf("print('hello')"),
            description = "A test recipe",
            dependencies = listOf("Python 3.x"),
            evidence = "Screenshot attached",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val jsonStr = json.encodeToString(Recipe.serializer(), recipe)
        val decoded = json.decodeFromString(Recipe.serializer(), jsonStr)

        assertEquals(recipe.id, decoded.id)
        assertEquals(recipe.userId, decoded.userId)
        assertEquals(recipe.title, decoded.title)
        assertEquals(recipe.languageTag, decoded.languageTag)
        assertEquals(recipe.secondaryTag, decoded.secondaryTag)
        assertEquals(recipe.code, decoded.code)
        assertEquals(recipe.description, decoded.description)
        assertEquals(recipe.dependencies, decoded.dependencies)
        assertEquals(recipe.evidence, decoded.evidence)
        assertEquals(recipe.createdAt, decoded.createdAt)
        assertEquals(recipe.updatedAt, decoded.updatedAt)
    }

    @Test
    fun `serialization handles null secondaryTag`() {
        val recipe = Recipe(
            id = "abc-123",
            userId = "user@ex.com",
            title = "No Tag",
            secondaryTag = null
        )

        val jsonStr = json.encodeToString(Recipe.serializer(), recipe)
        val decoded = json.decodeFromString(Recipe.serializer(), jsonStr)

        assertNull(decoded.secondaryTag)
    }

    @Test
    fun `serialization handles empty code list`() {
        val recipe = Recipe(
            id = "abc-123",
            userId = "user@ex.com",
            title = "Empty Code"
        )

        assertTrue(recipe.code.isEmpty(), "code should default to empty list")
        val jsonStr = json.encodeToString(Recipe.serializer(), recipe)
        val decoded = json.decodeFromString(Recipe.serializer(), jsonStr)
        assertTrue(decoded.code.isEmpty())
    }

    // ─── Empty factory ─────────────────────────────────────────────────

    @Test
    fun `empty factory generates non-blank UUID`() {
        val recipe = Recipe.empty(userId = "user@ex.com")

        assertNotNull(recipe.id)
        assertTrue(recipe.id.isNotBlank(), "id should be a non-blank UUID string")
        assertTrue(recipe.id.contains("-"), "id should look like a UUID")
    }

    @Test
    fun `empty factory sets userId correctly`() {
        val recipe = Recipe.empty(userId = "user@ex.com")

        assertEquals("user@ex.com", recipe.userId)
    }

    @Test
    fun `empty factory with different userId`() {
        val recipe = Recipe.empty(userId = "another@domain.org")

        assertEquals("another@domain.org", recipe.userId)
    }

    @Test
    fun `empty factory sets defaults for all other fields`() {
        val recipe = Recipe.empty(userId = "user@ex.com")

        assertEquals("", recipe.title)
        assertEquals("", recipe.languageTag)
        assertNull(recipe.secondaryTag)
        assertTrue(recipe.code.isEmpty())
        assertEquals("", recipe.description)
        assertTrue(recipe.dependencies.isEmpty())
        assertEquals("", recipe.evidence)
        assertTrue(recipe.createdAt >= 0L)
        assertTrue(recipe.updatedAt >= 0L)
    }

    @Test
    fun `each empty call generates unique id`() {
        val recipe1 = Recipe.empty(userId = "user@ex.com")
        val recipe2 = Recipe.empty(userId = "user@ex.com")

        assertNotEquals(recipe1.id, recipe2.id, "each empty() call should generate a unique UUID")
    }

    // ─── Field defaults ────────────────────────────────────────────────

    @Test
    fun `field defaults are correct with required fields only`() {
        val recipe = Recipe(
            id = "abc-123",
            userId = "user@ex.com"
        )

        assertEquals("abc-123", recipe.id)
        assertEquals("user@ex.com", recipe.userId)
        assertEquals("", recipe.title)
        assertEquals("", recipe.languageTag)
        assertNull(recipe.secondaryTag)
        assertEquals(emptyList(), recipe.code)
        assertEquals("", recipe.description)
        assertEquals(emptyList(), recipe.dependencies)
        assertEquals("", recipe.evidence)
        assertEquals(0L, recipe.createdAt)
        assertEquals(0L, recipe.updatedAt)
    }

    // ─── SnippetCardData adapter ───────────────────────────────────────

    @Test
    fun `SnippetCardData toRecipe maps fields correctly`() {
        val card = SnippetCardData(
            title = "Test Recipe",
            languageTag = "Kotlin",
            secondaryTag = "Mobile",
            iconAccent = Color(0xFF6C63FF),
            codeLines = listOf("fun main() {}"),
            footerLabel = "Evidence",
            footerIcon = Icons.Filled.Visibility,
            activeLineIndex = -1,
            showTerminalOutput = false,
            terminalLines = emptyList()
        )

        val recipe = card.toRecipe(userId = "user@domain.com")

        assertEquals(card.title, recipe.title)
        assertEquals(card.languageTag, recipe.languageTag)
        assertEquals(card.secondaryTag, recipe.secondaryTag)
        assertEquals(card.codeLines, recipe.code)
        assertEquals("user@domain.com", recipe.userId)
        assertNotNull(recipe.id, "toRecipe should generate an ID")
        assertTrue(recipe.id.isNotBlank())
    }

    @Test
    fun `Recipe toSnippetCardData maps fields correctly`() {
        val recipe = Recipe(
            id = "abc-123",
            userId = "user@ex.com",
            title = "My Recipe",
            languageTag = "Python",
            secondaryTag = "Data",
            code = listOf("print('hello')"),
            description = "A test recipe",
            dependencies = listOf("Python 3.x"),
            evidence = "Screenshot",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val card = recipe.toSnippetCardData()

        assertEquals(recipe.title, card.title)
        assertEquals(recipe.languageTag, card.languageTag)
        assertEquals(recipe.secondaryTag, card.secondaryTag)
        assertEquals(recipe.code, card.codeLines)
    }
}
