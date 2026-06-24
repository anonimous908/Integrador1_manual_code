package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.example.project.presentation.components.WelcomeContent
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
        WelcomeContent(email)
    }
}
