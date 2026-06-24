package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.components.PlaceholderContent
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

object RecentTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.sidebar_recent)
            val icon = rememberVectorPainter(Icons.Default.History)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        PlaceholderContent(stringResource(Res.string.placeholder_content, "Recientes"))
    }
}
