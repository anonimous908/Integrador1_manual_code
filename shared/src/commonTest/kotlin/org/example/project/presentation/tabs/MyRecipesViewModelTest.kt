package org.example.project.presentation.tabs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.project.data.repository.FakeRecipeRepositoryImpl
import org.example.project.domain.model.Recipe
import org.example.project.domain.repository.RecipeRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MyRecipesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── MyRecipesState defaults ────────────────────────────────────────

    @Test
    fun `myRecipesState defaults are correct`() {
        val state = MyRecipesState()

        assertTrue(state.recipes.isEmpty(), "recipes should default to empty list")
        assertEquals(true, state.isLoading, "isLoading should default to true")
        assertNull(state.error, "error should default to null")
        assertEquals(false, state.isListView, "isListView should default to false")
    }

    // ─── Initial state ──────────────────────────────────────────────────

    @Test
    fun `initial state is Loading`() {
        val repo = FakeRecipeRepositoryImpl()
        val viewModel = MyRecipesViewModel(repo)

        assertEquals(true, viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.recipes.isEmpty())
        assertNull(viewModel.state.value.error)
    }

    // ─── Loads recipes on init ─────────────────────────────────────────

    @Test
    fun `loads recipes on init and transitions to Success`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe1 = Recipe.empty(userId = "u1")
        val recipe2 = Recipe.empty(userId = "u2")
        repo.create(recipe1)
        repo.create(recipe2)

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.recipes.size)
        assertEquals(false, viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loads recipes with different data set`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        repo.create(Recipe.empty(userId = "u1"))

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.recipes.size)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    // ─── Error state ────────────────────────────────────────────────────

    @Test
    fun `error state on repository failure`() = runTest {
        val repo = object : RecipeRepository {
            override suspend fun getPersonalRecipes(): List<Recipe> =
                throw RuntimeException("Database connection failed")
            override suspend fun getById(id: String): Recipe? = null
            override suspend fun create(recipe: Recipe): Recipe = recipe
            override suspend fun update(recipe: Recipe): Recipe = recipe
            override suspend fun delete(id: String): Boolean = false
        }

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.recipes.isEmpty())
    }

    @Test
    fun `error state with different exception message`() = runTest {
        val repo = object : RecipeRepository {
            override suspend fun getPersonalRecipes(): List<Recipe> =
                throw RuntimeException("Permission denied")
            override suspend fun getById(id: String): Recipe? = null
            override suspend fun create(recipe: Recipe): Recipe = recipe
            override suspend fun update(recipe: Recipe): Recipe = recipe
            override suspend fun delete(id: String): Boolean = false
        }

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Permission denied", viewModel.state.value.error)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    // ─── ToggleView ────────────────────────────────────────────────────

    @Test
    fun `toggleView switches isListView from false to true`() {
        val repo = FakeRecipeRepositoryImpl()
        val viewModel = MyRecipesViewModel(repo)

        assertEquals(false, viewModel.state.value.isListView)

        viewModel.onEvent(MyRecipesEvent.ToggleView)

        assertEquals(true, viewModel.state.value.isListView)
    }

    @Test
    fun `toggleView switches back from true to false`() {
        val repo = FakeRecipeRepositoryImpl()
        val viewModel = MyRecipesViewModel(repo)

        viewModel.onEvent(MyRecipesEvent.ToggleView)
        viewModel.onEvent(MyRecipesEvent.ToggleView)

        assertEquals(false, viewModel.state.value.isListView)
    }

    // ─── DeleteRecipe ───────────────────────────────────────────────────

    @Test
    fun `deleteRecipe removes recipe and reloads`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe1 = Recipe.empty(userId = "u1")
        val recipe2 = Recipe.empty(userId = "u2")
        val recipe3 = Recipe.empty(userId = "u3")
        repo.create(recipe1)
        repo.create(recipe2)
        repo.create(recipe3)

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(3, viewModel.state.value.recipes.size)

        viewModel.onEvent(MyRecipesEvent.DeleteRecipe(recipe2.id))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.recipes.size)
        assertEquals(false, viewModel.state.value.recipes.any { it.id == recipe2.id })
    }

    @Test
    fun `deleteRecipe with non-existent id preserves all recipes`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "u1")
        repo.create(recipe)

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(MyRecipesEvent.DeleteRecipe("non-existent-id"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.recipes.size)
    }

    // ─── Refresh ────────────────────────────────────────────────────────

    @Test
    fun `refresh reloads recipes from repository`() = runTest {
        val repo = FakeRecipeRepositoryImpl()
        val recipe = Recipe.empty(userId = "u1")
        repo.create(recipe)

        val viewModel = MyRecipesViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, viewModel.state.value.recipes.size)

        // Add another recipe externally
        repo.create(Recipe.empty(userId = "u2"))

        viewModel.onEvent(MyRecipesEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.recipes.size)
    }
}
