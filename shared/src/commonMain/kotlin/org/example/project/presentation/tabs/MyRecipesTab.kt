package org.example.project.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.domain.model.Recipe
import org.example.project.domain.model.toSnippetCardData
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.presentation.AISearchScreen
import org.example.project.presentation.components.SnippetCard
import org.example.project.presentation.components.SnippetListItem
import org.example.project.presentation.theme.CodeNestColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlinproject.shared.generated.resources.*
import kotlinx.coroutines.delay

data class MyRecipesTab(val email: String) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.sidebar_my_recipes)
            val icon = rememberVectorPainter(Icons.Default.Book)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinInject<MyRecipesViewModel>()
        val syntaxHighlighter = koinInject<SyntaxHighlighter>()
        val state by viewModel.state.collectAsState()

        var searchQuery by remember { mutableStateOf("") }
        var showCopiedToast by remember { mutableStateOf(false) }

        // Filtrar recetas por título, lenguaje o etiqueta
        val filteredRecipes = remember(searchQuery, state.recipes) {
            if (searchQuery.isBlank()) {
                state.recipes
            } else {
                val query = searchQuery.lowercase().trim()
                state.recipes.filter { recipe ->
                    recipe.title.lowercase().contains(query) ||
                            recipe.languageTag.lowercase().contains(query) ||
                            (recipe.secondaryTag?.lowercase()?.contains(query) == true) ||
                            recipe.code.any { it.lowercase().contains(query) }
                }
            }
        }

        // Adaptar a SnippetCardData para el componente grid existente
        @Suppress("DEPRECATION")
        val cards = remember(filteredRecipes) {
            filteredRecipes.map { it.toSnippetCardData() }
        }

        Box(modifier = Modifier.fillMaxSize().background(CodeNestColors.background)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TopNavBar
                TopNavBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onAISearchClick = {
                        navigator.parent?.push(AISearchScreen())
                    }
                )

                // ContentCanvas with ViewModel-driven state
                ContentCanvas(
                    cards = cards,
                    recipes = filteredRecipes,
                    isListView = state.isListView,
                    isLoading = state.isLoading,
                    error = state.error,
                    syntaxHighlighter = syntaxHighlighter,
                    onCopied = {
                        showCopiedToast = true
                    },
                    onToggleView = { viewModel.onEvent(MyRecipesEvent.ToggleView) },
                    onDeleteRecipe = { id -> viewModel.onEvent(MyRecipesEvent.DeleteRecipe(id)) },
                    onRefresh = { viewModel.onEvent(MyRecipesEvent.Refresh) },
                    onRecipeClick = { /* TODO: navigate to detail screen */ }
                )
            }

            // Alerta flotante de "Copiado al portapapeles"
            if (showCopiedToast) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    showCopiedToast = false
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CodeNestColors.secondaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "¡Código copiado al portapapeles!",
                        color = CodeNestColors.onSecondaryContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TopNavBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onAISearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(CodeNestColors.surfaceContainer)
            .border(width = 1.dp, color = CodeNestColors.outlineVariant)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = CodeNestColors.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            "SnippetVault",
            color = CodeNestColors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(24.dp))

        // Barra de búsqueda
        Row(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(CodeNestColors.surfaceContainerLowest)
                .border(width = 1.dp, color = CodeNestColors.outlineVariant, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = CodeNestColors.outline)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        "Buscar recetas, etiquetas o código...",
                        color = CodeNestColors.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(color = CodeNestColors.onSurface, fontSize = 14.sp),
                    cursorBrush = SolidColor(CodeNestColors.primary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Botón "Buscar con IA"
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(CodeNestColors.surfaceContainerHighest)
                .border(width = 1.dp, color = CodeNestColors.outlineVariant, shape = RoundedCornerShape(6.dp))
                .clickable { onAISearchClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Psychology, contentDescription = null, tint = CodeNestColors.primary)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buscar con IA", color = CodeNestColors.primary, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))
        IconButtonSlot(icon = Icons.Filled.AddBox)
        IconButtonSlot(icon = Icons.Filled.Notifications, hasDot = true)
        IconButtonSlot(icon = Icons.Filled.AccountCircle)
    }
}

@Composable
private fun IconButtonSlot(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    hasDot: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(start = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = CodeNestColors.onSurfaceVariant)
        if (hasDot) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(CodeNestColors.primary)
            )
        }
    }
}

@Composable
@Suppress("DEPRECATION")
private fun ContentCanvas(
    cards: List<org.example.project.domain.model.SnippetCardData>,
    recipes: List<Recipe>,
    isListView: Boolean,
    isLoading: Boolean,
    error: String?,
    syntaxHighlighter: SyntaxHighlighter,
    onCopied: () -> Unit,
    onToggleView: () -> Unit,
    onDeleteRecipe: (String) -> Unit,
    onRefresh: () -> Unit,
    onRecipeClick: (Recipe) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Encabezado: título + acciones (Grid/List + Filter)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "My Recipes",
                    color = CodeNestColors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Manage and organize your personal code snippets.",
                    color = CodeNestColors.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Grid/List toggle (clickable segmented control)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CodeNestColors.surfaceContainerLowest)
                        .border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(6.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .then(
                                if (!isListView) Modifier.background(CodeNestColors.surfaceContainerHighest)
                                else Modifier
                            )
                            .clickable { if (isListView) onToggleView() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Grid",
                            color = if (!isListView) CodeNestColors.onSurface else CodeNestColors.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .then(
                                if (isListView) Modifier.background(CodeNestColors.surfaceContainerHighest)
                                else Modifier
                            )
                            .clickable { if (!isListView) onToggleView() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "List",
                            color = if (isListView) CodeNestColors.onSurface else CodeNestColors.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, CodeNestColors.outlineVariant, RoundedCornerShape(6.dp))
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = null, tint = CodeNestColors.onSurface)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filter", color = CodeNestColors.onSurface, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = CodeNestColors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Loading recipes...",
                            color = CodeNestColors.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ErrorOutline,
                            contentDescription = null,
                            tint = CodeNestColors.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Error loading recipes",
                            color = CodeNestColors.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            error,
                            color = CodeNestColors.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            }
            recipes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No se encontraron snippets para tu búsqueda.",
                        color = CodeNestColors.onSurfaceVariant,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            isListView -> {
                // List mode: compact rows with title + tags, no code preview
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recipes, key = { it.id }) { recipe ->
                        SnippetListItem(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe) }
                        )
                    }
                }
            }
            else -> {
                // Grid mode: existing card layout
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 320.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cards, key = { it.title + it.codeLines.hashCode() }) { card ->
                        SnippetCard(
                            card = card,
                            syntaxHighlighter = syntaxHighlighter,
                            onCopied = onCopied
                        )
                    }
                }
            }
        }
    }
}
