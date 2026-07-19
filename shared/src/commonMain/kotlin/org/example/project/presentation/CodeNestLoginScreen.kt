package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
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
import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.components.CodeNestTextField
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import org.example.project.presentation.theme.CodeNestLoginBrand
import kotlinproject.shared.generated.resources.*
import org.example.project.AppConfig
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.login.LoginEvent
import org.example.project.presentation.login.LoginState
import org.example.project.presentation.login.rememberGoogleSignInLauncher
import org.example.project.presentation.components.GoogleSignInButton
import org.example.project.presentation.components.CodeNestTextField
import org.example.project.presentation.components.PasswordTextField
import org.example.project.presentation.components.ErrorBanner
import org.example.project.presentation.components.EmailDivider

class CodeNestLoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: LoginViewModel = koinViewModel()
        val appViewModel: AppViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val appState by appViewModel.state.collectAsState()

        val launchGoogleSignIn = rememberGoogleSignInLauncher(
            onTokenReceived = { token ->
                viewModel.onEvent(LoginEvent.LoginWithGoogle(token))
            },
            onError = { _ -> }
        )

        LaunchedEffect(viewModel) {
            viewModel.state.collect { state ->
                if (state.isLoggedIn) {
                    viewModel.onEvent(LoginEvent.Reset)
                    navigator.replaceAll(HomeScreen(state.user?.email ?: state.email))
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeNestColors.background)
        ) {
            val scaleFactor = remember(maxWidth) { (maxWidth / 480.dp).coerceIn(0.5f, 1.5f) }
            val glowSize = remember(maxWidth, maxHeight) { (minOf(maxWidth, maxHeight) * 0.7f).coerceIn(200.dp, 800.dp) }
            val primaryContainer = CodeNestColors.primaryContainer
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

            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = (maxWidth * 0.85f).coerceIn(320.dp, 500.dp))
                    .padding(horizontal = 16.dp * scaleFactor),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                color = CodeNestColors.surfaceContainer.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp * scaleFactor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp * scaleFactor)
                        .background(CodeNestColors.surfaceContainerHigh, RoundedCornerShape(8.dp))
                        .border(1.dp, CodeNestColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal Icon",
                        tint = CodeNestColors.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp * scaleFactor))

                Text(
                    text = "CodeNest",
                    color = CodeNestColors.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp * scaleFactor))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = CodeNestColors.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part1))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = CodeNestColors.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part2))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = CodeNestColors.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part3))
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = CodeNestColors.onSurfaceVariant)) {
                            append(stringResource(Res.string.login_slogan_part4))
                        }
                    },
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp * scaleFactor))

                GoogleSignInButton(onClick = launchGoogleSignIn)
                Spacer(modifier = Modifier.height(16.dp * scaleFactor))
                
                EmailDivider(text = stringResource(Res.string.login_divider))

                CodeNestLoginForm(viewModel, scaleFactor, state)

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
                        Text(stringResource(Res.string.no_account_prompt), color = CodeNestColors.onSurfaceVariant, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp * scaleFactor))
                        Text(
                            text = stringResource(Res.string.create_account_link),
                            color = CodeNestColors.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(CreateAccountScreen()) }
                        )
                    }

                    PlatformLoginActions(navigator = navigator)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Términos y Condiciones",
                            color = CodeNestColors.onSurfaceVariant,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(TermsAndConditionsScreen()) }
                        )
                        Text(
                            text = " | ",
                            color = CodeNestColors.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Política de Privacidad",
                            color = CodeNestColors.onSurfaceVariant,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigator.push(PrivacyPolicyScreen()) }
                        )
                    }
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
                            (CodeNestLoginBrand.logoSize * scaleFactor)
                                .coerceIn(CodeNestLoginBrand.logoMin, CodeNestLoginBrand.logoMax)
                        )
                    )
                    Spacer(modifier = Modifier.height(CodeNestLoginBrand.logoTextSpacer * scaleFactor))
                    Text(
                        text = "Ztrene Studios",
                        color = CodeNestColors.onSurfaceVariant,
                        fontSize = (CodeNestLoginBrand.textSize * scaleFactor)
                            .coerceIn(CodeNestLoginBrand.textMin, CodeNestLoginBrand.textMax).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val latestUrl = appState.latestVersionUrl
                    if (appState.updateAvailable && latestUrl != null) {
                        val uriHandler = LocalUriHandler.current
                        Text(
                            text = "✨ ¡Nueva versión disponible!",
                            color = CodeNestColors.primary,
                            fontSize = (12 * scaleFactor).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    uriHandler.openUri(latestUrl)
                                }
                        )
                    } else {
                        Text(
                            text = "version ${AppConfig.VERSION}",
                            color = CodeNestColors.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = (10 * scaleFactor).sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

internal fun calculateBrandPadding(totalSize: Dp): Dp = maxOf(16.dp, totalSize * 0.03f)

@Composable
fun CodeNestLoginForm(
    viewModel: LoginViewModel,
    scaleFactor: Float,
    state: LoginState
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        ErrorBanner(errorMessage = state.errorMessage)
        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        CodeNestTextField(
            label = stringResource(Res.string.email_label),
            value = state.email,
            onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
            placeholder = stringResource(Res.string.email_placeholder),
            keyboardType = KeyboardType.Email,
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = CodeNestColors.outline, modifier = Modifier.size(18.dp * scaleFactor)) },
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
            color = CodeNestColors.primary,
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
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CodeNestColors.primary,
            contentColor = CodeNestColors.onPrimary,
            disabledContainerColor = CodeNestColors.primary.copy(alpha = 0.5f)
        ),
        enabled = !state.isLoading
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp * scaleFactor),
                color = CodeNestColors.onPrimary,
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
