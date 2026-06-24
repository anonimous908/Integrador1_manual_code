package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.components.PlaceholderContent
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

object CommunityExamplesTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.sidebar_community_examples)
            val icon = rememberVectorPainter(Icons.Default.Group)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        PlaceholderContent(stringResource(Res.string.placeholder_content, "Manuales Compartidos"))
    }
}
