package org.example.project.presentation.tabs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.domain.repository.RecipeRepository
import org.example.project.presentation.base.BaseViewModel

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
) : BaseViewModel() {

    private val _state = MutableStateFlow(MyRecipesState())
    val state: StateFlow<MyRecipesState> = _state.asStateFlow()

    init { loadRecipes() }

    fun onEvent(event: MyRecipesEvent) {
        when (event) {
            MyRecipesEvent.ToggleView    -> _state.update { it.copy(isListView = !it.isListView) }
            is MyRecipesEvent.DeleteRecipe -> deleteRecipe(event.id)
            MyRecipesEvent.Refresh       -> loadRecipes()
        }
    }

    private fun safeLaunch(block: suspend () -> Unit) = launchSafely(
        setLoading = { loading -> _state.update { it.copy(isLoading = loading, error = null) } },
        onError = { msg -> _state.update { it.copy(error = msg) } },
        block = block
    )

    private fun loadRecipes() {
        safeLaunch {
            _state.update { it.copy(recipes = repository.getPersonalRecipes()) }
        }
    }

    private fun deleteRecipe(id: String) {
        safeLaunch {
            repository.delete(id)
            loadRecipes()
        }
    }
}
