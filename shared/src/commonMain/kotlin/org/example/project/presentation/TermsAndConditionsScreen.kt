package org.example.project.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.project.presentation.theme.DevSpaceColors
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

import org.jetbrains.compose.resources.stringResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.terms_content
import kotlinproject.shared.generated.resources.terms_title

class TermsAndConditionsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.terms_title), fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DevSpaceColors.background,
                        titleContentColor = DevSpaceColors.onSurface,
                        navigationIconContentColor = DevSpaceColors.onSurface
                    )
                )
            },
            containerColor = DevSpaceColors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(Res.string.terms_content),
                    color = DevSpaceColors.onSurfaceVariant,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

