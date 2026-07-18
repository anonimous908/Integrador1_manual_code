package org.example.project.presentation.tabs

import org.example.project.domain.model.Recipe

/**
 * UI state for [MyRecipesViewModel].
 *
 * @property recipes Current recipe list from the repository.
 * @property isLoading True while data is being loaded from the repository.
 * @property error Error message when loading fails, null otherwise.
 * @property isListView Grid (false) or List (true) view toggle state.
 */
data class MyRecipesState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isListView: Boolean = false
)

/**
 * User intents processed by [MyRecipesViewModel].
 */
sealed interface MyRecipesEvent {
    /** Toggle between grid and list view mode. */
    data object ToggleView : MyRecipesEvent

    /** Delete a recipe by its [id]. */
    data class DeleteRecipe(val id: String) : MyRecipesEvent

    /** Reload recipes from the repository. */
    data object Refresh : MyRecipesEvent
}
