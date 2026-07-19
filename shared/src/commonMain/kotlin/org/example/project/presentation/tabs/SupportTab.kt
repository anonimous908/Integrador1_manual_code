package org.example.project.presentation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import org.example.project.presentation.base.PlaceholderTab
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.sidebar_support

object SupportTab : PlaceholderTab(
    tabIndex = 5u,
    titleRes = Res.string.sidebar_support,
    iconVector = Icons.Default.HelpOutline,
    contentTitle = "Soporte",
    contentSubtitle = "Encuentra ayuda, reporta problemas o contacta con nuestro equipo."
)

