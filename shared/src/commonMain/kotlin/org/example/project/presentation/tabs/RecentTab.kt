package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import org.example.project.presentation.base.PlaceholderTab
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.sidebar_recent

object RecentTab : PlaceholderTab(
    tabIndex = 2u,
    titleRes = Res.string.sidebar_recent,
    iconVector = Icons.Default.History,
    contentTitle = "Reciente",
    contentSubtitle = "Tus snippets y recetas vistas recientemente."
)

