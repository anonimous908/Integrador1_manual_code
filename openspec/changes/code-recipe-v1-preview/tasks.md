# Tasks: Code Recipe V1 Preview

## Review Workload Forecast

| Field | Value |
|-------|-------|
| Estimated changed lines | ~700–850 |
| 400-line budget risk | High |
| Chained PRs recommended | Yes |
| Suggested split | PR 1 (Domain + Storage) → PR 2 (ViewModel + UI) |
| Delivery strategy | ask-on-risk |
| Chain strategy | stacked-to-main |

Decision needed before apply: Yes
Chained PRs recommended: Yes
Chain strategy: stacked-to-main (PR 1 of 2 completed)
400-line budget risk: High

### Suggested Work Units

| Unit | Goal | Likely PR | Focused test command | Runtime harness | Rollback boundary |
|------|------|-----------|----------------------|-----------------|-------------------|
| 1 | Domain model + repository impls + DI | PR 1 | `./gradlew :shared:allTests` | `./gradlew :shared:allTests` | Revert commits touching domain/, data/repository/*Impl.kt, di/AppModule.kt |
| 2 | ViewModel + tab/screen + list UI | PR 2 | `./gradlew :shared:allTests` | Run desktopApp, create recipe, toggle grid/list | Revert commits touching presentation/ |

## Phase 1: Domain Foundation

- [x] 1.1 RED: Write `domain/model/RecipeTest.kt` — serialization roundtrip, `empty()` factory, `SnippetDetail.toRecipe()` adapter, nullable/missing fields
- [x] 1.2 GREEN: Create `domain/model/Recipe.kt` — `@Serializable` data class, `companion.empty()` via `kotlin.uuid.Uuid.random()`, `SnippetDetail.toRecipe(userId)` extension, `Recipe.toSnippetCardData()` adapter
- [x] 1.3 UPDATE: `domain/model/SnippetCardData.kt` — add `@Deprecated("Use Recipe")`
- [x] 1.4 UPDATE: `domain/repository/RecipeRepository.kt` — add `getById`, `create`, `update`, `delete` suspend functions returning `Recipe`; change `getPersonalRecipes()` to `List<Recipe>`

## Phase 2: Storage + Fakes

- [x] 2.1 RED: Write settings-based CRUD test — create/update/delete/getById/getPersonalRecipes via `InMemorySettings`
- [x] 2.2 GREEN: Create `data/repository/SettingsRecipeRepositoryImpl.kt` — single `recipe_all` JSON key, `Settings.getStringOrNull`/`putString`, `Json` codec
- [x] 2.3 GREEN: Create `data/repository/FakeRecipeRepositoryImpl.kt` — `MutableMap<String, Recipe>` in-memory, all CRUD ops
- [x] 2.4 UPDATE: `data/repository/MockRecipeRepositoryImpl.kt` — return `List<Recipe>` (drop Compose UI fields, map to Recipe)

## Phase 3: ViewModel

- [ ] 3.1 RED: Write `presentation/tabs/MyRecipesTabTest.kt` — load on init, loading state, error, `ToggleView` flips `isListView`, `DeleteRecipe` removes, `Refresh` reloads
- [ ] 3.2 GREEN: Create `presentation/tabs/MyRecipesState.kt` — `MyRecipesState` data class (recipes, isLoading, error, isListView) + `MyRecipesEvent` sealed interface (ToggleView, DeleteRecipe, Refresh)
- [ ] 3.3 GREEN: Create `presentation/tabs/MyRecipesViewModel.kt` — constructor(repo), `state: StateFlow<MyRecipesState>`, `onEvent()` dispatch, load on `init` via `viewModelScope.launch`

## Phase 4: UI Integration

- [ ] 4.1 GREEN: Create `presentation/components/SnippetListItem.kt` — list-mode row composable (title + `languageTag`/`secondaryTag` pills), no code preview
- [ ] 4.2 UPDATE: `presentation/components/SnippetComponents.kt` — `SnippetCard` overload accepting `Recipe` (map fields for display), keep backward-compat `SnippetCardData` variant
- [ ] 4.3 UPDATE: `presentation/tabs/MyRecipesTab.kt` — inject `MyRecipesViewModel` via `koinInject()`, observe `state`, conditional `LazyVerticalGrid`/`LazyColumn`, toggle in toolbar, inline search filter preserved
- [ ] 4.4 UPDATE: `presentation/SnippetDetailScreen.kt` — inject `RecipeRepository`, map `SnippetDetail` → `Recipe` via adapter, "Guardar" calls `create()`/`update()`, navigates back via `tabNavigator.current`

## Phase 5: DI Wiring

- [x] 5.1 UPDATE: `di/AppModule.kt` — replace `single<RecipeRepository>` with `SettingsRecipeRepositoryImpl(settings = get())`, add `factoryOf(::MyRecipesViewModel)`
