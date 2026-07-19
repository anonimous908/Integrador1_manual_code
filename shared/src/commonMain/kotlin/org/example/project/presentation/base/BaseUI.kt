package org.example.project.presentation.base

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.theme.CodeNestColors
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Clase base reutilizable para pantallas estáticas de información (documentos, privacidad, términos).
 * Elimina la duplicación de Scaffolds, barras superiores, botones de regreso y scroll containers.
 */
open class SimpleDocumentScreen(
    private val titleRes: StringResource,
    private val contentRes: StringResource
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(titleRes), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CodeNestColors.background,
                        titleContentColor = CodeNestColors.onSurface,
                        navigationIconContentColor = CodeNestColors.onSurface
                    )
                )
            },
            containerColor = CodeNestColors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(contentRes),
                    color = CodeNestColors.onSurfaceVariant,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

/**
 * Clase base reutilizable para pestañas estáticas o en construcción (Placeholders).
 * Reduce la definición de pestañas repetitivas a una sola declaración limpia.
 */
open class PlaceholderTab(
    private val tabIndex: UShort,
    private val titleRes: StringResource,
    private val iconVector: ImageVector,
    private val contentTitle: String,
    private val contentSubtitle: String
) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(titleRes)
            val icon = rememberVectorPainter(iconVector)
            return remember {
                TabOptions(
                    index = tabIndex,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        PlaceholderContent(
            icon = iconVector,
            title = contentTitle,
            subtitle = contentSubtitle
        )
    }
}

@Composable
fun PlaceholderContent(icon: ImageVector, title: String, subtitle: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CodeNestColors.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = title,
                color = CodeNestColors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                color = CodeNestColors.onSurfaceVariant,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/** Composable reutilizable para estado de carga centrado. */
@Composable
fun LoadingState(message: String = "Cargando...") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = CodeNestColors.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp)
        }
    }
}

/** Composable reutilizable para estado de error centrado con botón de reintento. */
@Composable
fun ErrorState(message: String, onRetry: (() -> Unit)? = null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = CodeNestColors.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error", color = CodeNestColors.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(message, color = CodeNestColors.onSurfaceVariant, fontSize = 14.sp, textAlign = TextAlign.Center)
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onRetry) { Text("Reintentar") }
            }
        }
    }
}
