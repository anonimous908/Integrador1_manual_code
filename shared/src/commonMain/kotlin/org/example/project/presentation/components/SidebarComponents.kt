package org.example.project.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.theme.DevSpaceColors

@Composable
fun DevSpaceSidebar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    email: String,
    onLogout: () -> Unit
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
                Text(text = "DevSpace", color = DevSpaceColors.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "v1.3.0", color = DevSpaceColors.onSurfaceVariant, fontSize = 12.sp)
            }
        }

        // New Snippet Button
        Button(
            onClick = { /* TODO */ },
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
            Text("New Snippet", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        // Navigation Items
        SidebarItem("My Recipes", Icons.Default.Book, selectedTab == "My Recipes") { onTabSelected("My Recipes") }
        SidebarItem("Community Examples", Icons.Default.Group, selectedTab == "Community Examples") { onTabSelected("Community Examples") }
        SidebarItem("Recent", Icons.Default.History, selectedTab == "Recent") { onTabSelected("Recent") }
        SidebarItem("Settings", Icons.Default.Settings, selectedTab == "Settings") { onTabSelected("Settings") }

        Spacer(modifier = Modifier.weight(1f))

        // Divider
        HorizontalDivider(
            color = DevSpaceColors.outlineVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Bottom Links
        SidebarItem("Documentation", Icons.Default.HelpOutline, selectedTab == "Documentation") { onTabSelected("Documentation") }
        SidebarItem("Support", Icons.Default.Info, selectedTab == "Support") { onTabSelected("Support") }

        Spacer(modifier = Modifier.height(16.dp))

        // User Profile
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
            Column {
                val name = email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                Text(text = name, color = DevSpaceColors.onSurface, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(text = "Pro Plan", color = DevSpaceColors.primary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SidebarItem(
    title: String,
    icon: ImageVector,
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
        Icon(icon, contentDescription = title, tint = contentColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, color = contentColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
    }
}
