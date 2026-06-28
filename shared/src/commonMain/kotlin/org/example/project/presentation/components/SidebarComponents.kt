package org.example.project.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.example.project.presentation.theme.DevSpaceColors

@Composable
fun DevSpaceSidebar(
    tabNavigator: TabNavigator,
    email: String,
    onLogout: () -> Unit,
    onNewSnippet: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(DevSpaceColors.surfaceContainerLow)
            .border(1.dp, DevSpaceColors.outlineVariant.copy(alpha = 0.3f))
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        // Logo and Brand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("{ }", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = stringResource(Res.string.app_name), color = DevSpaceColors.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = stringResource(Res.string.app_version), color = DevSpaceColors.onSurfaceVariant, fontSize = 12.sp)
            }
        }

        // New Snippet Button
        Button(
            onClick = onNewSnippet,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DevSpaceColors.primary,
                contentColor = DevSpaceColors.onPrimaryContainer
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = "New", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.new_snippet), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        // Navigation Items
        TabNavigationItem(org.example.project.presentation.tabs.MyRecipesTab(email), tabNavigator)
        TabNavigationItem(org.example.project.presentation.tabs.CommunityExamplesTab, tabNavigator)
        TabNavigationItem(org.example.project.presentation.tabs.RecentTab, tabNavigator)
        TabNavigationItem(org.example.project.presentation.tabs.SettingsTab, tabNavigator)

        Spacer(modifier = Modifier.weight(1f))

        // Divider
        HorizontalDivider(
            color = DevSpaceColors.outlineVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Bottom Links
        TabNavigationItem(org.example.project.presentation.tabs.DocumentationTab, tabNavigator)
        TabNavigationItem(org.example.project.presentation.tabs.SupportTab, tabNavigator)

        Spacer(modifier = Modifier.height(16.dp))

        // User Profile / Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onLogout() }
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(DevSpaceColors.surfaceContainerHigh, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "User Avatar", tint = DevSpaceColors.onSurfaceVariant, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                val name = email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                Text(text = name, color = DevSpaceColors.onSurface, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(text = stringResource(Res.string.logout), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun TabNavigationItem(tab: Tab, tabNavigator: TabNavigator) {
    val isSelected = tabNavigator.current.key == tab.key
    SidebarItem(
        title = tab.options.title,
        icon = tab.options.icon,
        isSelected = isSelected,
        onClick = { tabNavigator.current = tab }
    )
}

@Composable
private fun SidebarItem(
    title: String,
    icon: Painter?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) DevSpaceColors.primary else Color.Transparent
    val contentColor = if (isSelected) DevSpaceColors.onPrimaryContainer else DevSpaceColors.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = title, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(text = title, color = contentColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
    }
}
