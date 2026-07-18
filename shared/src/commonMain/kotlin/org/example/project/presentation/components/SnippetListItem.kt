package org.example.project.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.domain.model.Recipe
import org.example.project.presentation.theme.CodeNestColors

/**
 * A compact list-mode row for displaying a [Recipe].
 *
 * Shows the recipe title, language tag, and optional secondary tag as pill-style
 * chips. Does NOT display any code preview, code lines, or syntax highlighting.
 *
 * @param recipe The recipe to display.
 * @param onClick Called when the row is clicked (e.g., navigate to detail).
 */
@Composable
fun SnippetListItem(
    recipe: Recipe,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = recipe.title,
                color = CodeNestColors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TagChip(
                    text = recipe.languageTag,
                    accent = CodeNestColors.tertiary
                )
                if (!recipe.secondaryTag.isNullOrBlank()) {
                    TagChip(
                        text = recipe.secondaryTag,
                        accent = CodeNestColors.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = CodeNestColors.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun TagChip(text: String, accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(accent.copy(alpha = 0.15f))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = accent,
            fontSize = 10.sp
        )
    }
}
