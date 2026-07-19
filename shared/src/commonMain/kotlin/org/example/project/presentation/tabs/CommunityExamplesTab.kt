package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import org.example.project.presentation.base.PlaceholderTab
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.sidebar_community_examples

object CommunityExamplesTab : PlaceholderTab(
    tabIndex = 1u,
    titleRes = Res.string.sidebar_community_examples,
    iconVector = Icons.Default.Group,
    contentTitle = "Comunidad",
    contentSubtitle = "Ejemplos y snippets compartidos por la comunidad de CodeNest."
)

