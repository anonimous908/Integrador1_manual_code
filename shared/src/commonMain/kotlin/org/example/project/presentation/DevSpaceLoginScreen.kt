package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import org.example.project.presentation.theme.DevSpaceColors
import org.example.project.presentation.components.DevSpaceTextField
import org.example.project.presentation.components.PasswordTextField
import org.example.project.presentation.components.EmailDivider
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.jetbrains.compose.resources.stringResource
import kotlinproject.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.example.project.AppConfig
import org.example.project.presentation.theme.DevSpaceLoginBrand
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.login.LoginEvent
import org.example.project.presentation.login.LoginState

internal fun calculateBrandPadding(totalSize: Dp): Dp = maxOf(16.dp, totalSize * 0.03f)

class DevSpaceLoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.state.collect { state ->
                if (state.isLoggedIn) {
                    viewModel.onEvent(LoginEvent.Reset)
                    navigator.replace(HomeScreen(state.email))
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val scaleFactor = remember(maxWidth) { (maxWidth / 480.dp).coerceIn(0.5f, 1.5f) }
            val glowSize = remember(maxWidth, maxHeight) { (minOf(maxWidth, maxHeight) * 0.7f).coerceIn(200.dp, 800.dp) }
            val primaryContainer = MaterialTheme.colorScheme.primaryContainer
            val glowBrush = remember(primaryContainer) {
                Brush.radialGradient(
                    colors = listOf(
                        primaryContainer.copy(alpha = 0.08f),
                        Color.Transparent
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(glowSize)
                    .align(Alignment.Center)
                    .background(
                        brush = glowBrush,
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = (maxWidth * 0.85f).coerceIn(320.dp, 500.dp))
                    .padding(horizontal = 16.dp * scaleFactor)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp * scaleFactor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp * scaleFactor)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp * scaleFactor))

                Text(
                    text = "DevSpace",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp * scaleFactor))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part1))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part2))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part3))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part4))
                        }
                    },
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp * scaleFactor))

                OutlinedButton(
                    onClick = { /* TODO: Google Login */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DevSpaceColors.surfaceContainerHigh,
                        contentColor = DevSpaceColors.onSurface
                    ),
                    border = BorderStroke(1.dp, DevSpaceColors.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Image(
                        painter = painterResource(Res.drawable.google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(20.dp * scaleFactor)
                    )
                    Spacer(modifier = Modifier.width(8.dp * scaleFactor))
                    Text(stringResource(Res.string.google_login), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(16.dp * scaleFactor))
                
                EmailDivider(text = stringResource(Res.string.login_divider))

                DevSpaceLoginForm(viewModel, scaleFactor, state)

                Column(
                    modifier = Modifier
                        .widthIn(max = 420.dp * scaleFactor)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(Res.string.no_account_prompt), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp * scaleFactor))
                        Text(
                            text = stringResource(Res.string.create_account_link),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(CreateAccountScreen()) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Términos y Condiciones",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(TermsAndConditionsScreen()) }
                        )
                        Text(
                            text = " | ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Política de Privacidad",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(PrivacyPolicyScreen()) }
                        )
                    }
                }

            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = remember(maxHeight) { calculateBrandPadding(maxHeight) },
                        end = remember(maxWidth) { calculateBrandPadding(maxWidth) }
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(Res.drawable.Bison_Logo_Studios),
                        contentDescription = "Bison Studios Logo",
                        modifier = Modifier.size(
                            (DevSpaceLoginBrand.logoSize * scaleFactor)
                                .coerceIn(DevSpaceLoginBrand.logoMin, DevSpaceLoginBrand.logoMax)
                        )
                    )
                    Spacer(modifier = Modifier.height(DevSpaceLoginBrand.logoTextSpacer * scaleFactor))
                    Text(
                        text = "Ztrene Studios",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = (DevSpaceLoginBrand.textSize * scaleFactor)
                            .coerceIn(DevSpaceLoginBrand.textMin, DevSpaceLoginBrand.textMax).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val latestUrl = state.latestVersionUrl
                    if (state.updateAvailable && latestUrl != null) {
                        val uriHandler = LocalUriHandler.current
                        Text(
                            text = "✨ ¡Nueva versión disponible!",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = (12 * scaleFactor).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    uriHandler.openUri(latestUrl)
                                }
                                .padding(4.dp)
                        )
                    } else {
                        Text(
                            text = "version ${AppConfig.VERSION}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = (10 * scaleFactor).sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DevSpaceLoginForm(
    viewModel: LoginViewModel,
    scaleFactor: Float,
    state: LoginState
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        DevSpaceTextField(
            label = stringResource(Res.string.email_label),
            value = state.email,
            onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
            placeholder = stringResource(Res.string.email_placeholder),
            keyboardType = KeyboardType.Email,
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = DevSpaceColors.outline, modifier = Modifier.size(18.dp * scaleFactor)) },
            error = state.emailError
        )
    }

    Spacer(modifier = Modifier.height(16.dp * scaleFactor))

    Column(modifier = Modifier.fillMaxWidth()) {

        PasswordTextField(
            label = stringResource(Res.string.password_label),
            value = state.pass,
            onValueChange = { viewModel.onEvent(LoginEvent.PassChanged(it)) },
            visible = passwordVisible,
            onToggleVisible = { passwordVisible = !passwordVisible },
            error = state.passError
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(Res.string.forgot_password),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* TODO */ }
        )
    }

    Button(
        onClick = { viewModel.onEvent(LoginEvent.Submit) },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp * scaleFactor),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        enabled = !state.isLoading
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp * scaleFactor),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(Res.string.login_button),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
