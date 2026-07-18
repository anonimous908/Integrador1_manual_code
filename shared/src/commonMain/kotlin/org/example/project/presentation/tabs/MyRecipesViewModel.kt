package org.example.project.presentation.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.repository.RecipeRepository

/**
 * ViewModel for [MyRecipesTab].
 *
 * Manages recipe list state, loading, error handling, view mode toggling,
 * deletion, and refresh.
 *
 * @param repository The [RecipeRepository] to load, delete, and refresh recipes.
 */
class MyRecipesViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyRecipesState())
    val state: StateFlow<MyRecipesState> = _state.asStateFlow()

    init {
        loadRecipes()
    }

    /**
     * Processes [MyRecipesEvent] intents from the UI.
     */
    fun onEvent(event: MyRecipesEvent) {
        when (event) {
            MyRecipesEvent.ToggleView -> {
                _state.update { it.copy(isListView = !it.isListView) }
            }
            is MyRecipesEvent.DeleteRecipe -> {
                deleteRecipe(event.id)
            }
            MyRecipesEvent.Refresh -> {
                loadRecipes()
            }
        }
    }

    private fun loadRecipes() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val recipes = repository.getPersonalRecipes()
                _state.update {
                    it.copy(recipes = recipes, isLoading = false)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    private fun deleteRecipe(id: String) {
        viewModelScope.launch {
            try {
                repository.delete(id)
                loadRecipes()
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to delete")
                }
            }
        }
    }
}
