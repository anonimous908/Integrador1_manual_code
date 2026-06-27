package org.example.project.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import org.example.project.presentation.AISearchScreen
import org.example.project.presentation.components.SnippetCard
import org.example.project.presentation.components.SnippetCardData
import org.example.project.presentation.components.sampleCards
import org.example.project.presentation.theme.DevSpaceColors
import org.jetbrains.compose.resources.stringResource
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
        var searchQuery by remember { mutableStateOf("") }
        var showCopiedToast by remember { mutableStateOf(false) }

        // Filtrar tarjetas por título, lenguaje o etiquetas
        val filteredCards = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                sampleCards
            } else {
                val query = searchQuery.lowercase().trim()
                sampleCards.filter { card ->
                    card.title.lowercase().contains(query) ||
                            card.languageTag.lowercase().contains(query) ||
                            card.secondaryTag.lowercase().contains(query) ||
                            card.codeLines.any { it.lowercase().contains(query) }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(DevSpaceColors.background)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TopNavBar
                TopNavBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onAISearchClick = {
                        navigator.parent?.push(AISearchScreen())
                    }
                )

                // ContentCanvas
                ContentCanvas(
                    cards = filteredCards,
                    onCopied = {
                        showCopiedToast = true
                    }
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
                        .background(DevSpaceColors.secondaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "¡Código copiado al portapapeles!",
                        color = DevSpaceColors.onSecondaryContainer,
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
            .background(DevSpaceColors.surfaceContainer)
            .border(width = 1.dp, color = DevSpaceColors.outlineVariant)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = DevSpaceColors.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            "SnippetVault",
            color = DevSpaceColors.primary,
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
                .background(DevSpaceColors.surfaceContainerLowest)
                .border(width = 1.dp, color = DevSpaceColors.outlineVariant, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = DevSpaceColors.outline)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        "Search recipes, tags, or code...",
                        color = DevSpaceColors.outline,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(color = DevSpaceColors.onSurface, fontSize = 14.sp),
                    cursorBrush = SolidColor(DevSpaceColors.primary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Botón "Buscar con IA"
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(DevSpaceColors.surfaceContainerHighest)
                .border(width = 1.dp, color = DevSpaceColors.outlineVariant, shape = RoundedCornerShape(6.dp))
                .clickable { onAISearchClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Psychology, contentDescription = null, tint = DevSpaceColors.primary)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buscar con IA", color = DevSpaceColors.primary, fontSize = 12.sp)
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
        Icon(icon, contentDescription = null, tint = DevSpaceColors.onSurfaceVariant)
        if (hasDot) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(DevSpaceColors.primary)
            )
        }
    }
}

@Composable
private fun ContentCanvas(
    cards: List<SnippetCardData>,
    onCopied: () -> Unit
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
                    color = DevSpaceColors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Manage and organize your personal code snippets.",
                    color = DevSpaceColors.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DevSpaceColors.surfaceContainerLowest)
                        .border(1.dp, DevSpaceColors.outlineVariant, RoundedCornerShape(6.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(DevSpaceColors.surfaceContainerHighest)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) { Text("Grid", color = DevSpaceColors.onSurface, fontSize = 12.sp) }
                    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text("List", color = DevSpaceColors.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, DevSpaceColors.outlineVariant, RoundedCornerShape(6.dp))
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = null, tint = DevSpaceColors.onSurface)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filter", color = DevSpaceColors.onSurface, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cards.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No se encontraron snippets para tu búsqueda.",
                    color = DevSpaceColors.onSurfaceVariant,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Bento grid de snippets
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cards) { card ->
                    SnippetCard(card = card, onCopied = onCopied)
                }
            }
        }
    }
}
