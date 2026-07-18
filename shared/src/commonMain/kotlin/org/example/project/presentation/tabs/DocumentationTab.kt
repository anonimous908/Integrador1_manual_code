package org.example.project.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.domain.service.openLocalFile
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

object DocumentationTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.sidebar_documentation)
            val icon = rememberVectorPainter(Icons.AutoMirrored.Filled.MenuBook)
            return remember {
                TabOptions(
                    index = 4u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        PlaceholderContent(
            icon = Icons.Default.MenuBook,
            title = "Documentación",
            subtitle = "Explora guías, tutoriales y referencias técnicas para mejorar tu código."
        )
    }
}

