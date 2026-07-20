package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinproject.shared.generated.resources.*
import org.example.project.presentation.base.NavigateOnSuccess
import org.example.project.presentation.components.CodeNestFooter
import org.example.project.presentation.components.CodeNestTextField
import org.example.project.presentation.components.DecorativeGlow
import org.example.project.presentation.components.EmailDivider
import org.example.project.presentation.components.ErrorBanner
import org.example.project.presentation.components.GoogleSignInButton
import org.example.project.presentation.components.PasswordTextField
import org.example.project.presentation.components.TermsCheckbox
import org.example.project.presentation.login.rememberGoogleSignInLauncher
import org.example.project.presentation.register.RegisterEvent
import org.example.project.presentation.register.RegisterViewModel
import org.example.project.presentation.theme.CodeNestColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// ─── Screen wrapper ──────────────────────────────────────────────────────────

class CreateAccountScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: RegisterViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        val launchGoogleSignIn = rememberGoogleSignInLauncher(
            onTokenReceived = { token ->
                viewModel.onEvent(RegisterEvent.RegisterWithGoogle(token))
            },
            onError = { errorMsg ->
                viewModel.onEvent(RegisterEvent.RegisterWithGoogleError(errorMsg))
            }
        )

        NavigateOnSuccess(
            state = viewModel.state,
            isSuccess = { it.success },
            getEmail = { it.user?.email ?: it.email },
            onNavigate = { email -> navigator.replaceAll(HomeScreen(email)) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeNestColors.background)
        ) {
            DecorativeGlow(glowColor = CodeNestColors.primary.copy(alpha = 0.15f))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                RegisterCard(
                    errorMessage   = state.errorMessage,
                    name           = state.name,
                    email          = state.email,
                    password       = state.pass,
                    termsAccepted  = state.termsAccepted,
                    onCreateAccount = { viewModel.onEvent(RegisterEvent.Submit) },
                    onNameChange   = { viewModel.onEvent(RegisterEvent.NameChanged(it)) },
                    onEmailChange  = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                    onPasswordChange = { viewModel.onEvent(RegisterEvent.PassChanged(it)) },
                    onTermsChange  = { viewModel.onEvent(RegisterEvent.TermsAcceptedChanged(it)) },
                    onSignInWithGoogle = launchGoogleSignIn,
                    onNavigateToLogin = { navigator.pop() },
                    onTermsClick   = { navigator.push(TermsAndConditionsScreen()) },
                    onPrivacyClick = { navigator.push(PrivacyPolicyScreen()) },
                )

                Spacer(modifier = Modifier.height(24.dp))

                CodeNestFooter(
                    onTermsClick = { navigator.push(TermsAndConditionsScreen()) },
                    onPrivacyClick = { navigator.push(PrivacyPolicyScreen()) }
                )
            }
        }
    }
}

// ─── Presentational Card ──────────────────────────────────────────────────────

@Composable
private fun RegisterCard(
    errorMessage: String?,
    name: String,
    email: String,
    password: String,
    termsAccepted: Boolean,
    onCreateAccount: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onSignInWithGoogle: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CodeNestColors.surface)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(
                            CodeNestColors.primary.copy(alpha = 0.20f),
                            CodeNestColors.outlineVariant.copy(alpha = 0.50f),
                        )
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text       = stringResource(Res.string.create_account_title),
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 32.sp,
                    lineHeight = 40.sp,
                    letterSpacing = (-0.02).sp,
                    color      = CodeNestColors.onSurface,
                    textAlign  = TextAlign.Center,
                )
                Text(
                    text       = stringResource(Res.string.create_account_subtitle),
                    fontFamily = FontFamily.Default,
                    fontSize   = 14.sp,
                    color      = CodeNestColors.onSurfaceVariant,
                    textAlign  = TextAlign.Center,
                )
            }

            ErrorBanner(errorMessage = errorMessage)

            // Google button
            GoogleSignInButton(onClick = onSignInWithGoogle)

            // Divider
            EmailDivider()

            // Form fields
            CodeNestTextField(
                label       = stringResource(Res.string.full_name_label),
                value       = name,
                onValueChange = onNameChange,
                placeholder = stringResource(Res.string.full_name_placeholder),
                keyboardType = KeyboardType.Text,
            )
            CodeNestTextField(
                label       = stringResource(Res.string.email_label),
                value       = email,
                onValueChange = onEmailChange,
                placeholder = stringResource(Res.string.email_placeholder),
                keyboardType = KeyboardType.Email,
            )
            PasswordTextField(
                label           = stringResource(Res.string.password_label),
                value           = password,
                onValueChange   = onPasswordChange,
                visible         = passwordVisible,
                onToggleVisible = { passwordVisible = !passwordVisible },
            )

            // Terms checkbox
            TermsCheckbox(
                termsAccepted = termsAccepted,
                onTermsAcceptedChange = onTermsChange,
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick,
            )

            // Submit button
            Button(
                onClick  = onCreateAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = CodeNestColors.primary,
                    contentColor   = CodeNestColors.onPrimaryContainer,
                    disabledContainerColor = CodeNestColors.primary.copy(alpha = 0.5f),
                ),
                enabled  = termsAccepted,
            ) {
                Text(
                    text       = stringResource(Res.string.create_account_button),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 12.sp,
                )
            }

            // Already have an account
            Text(
                text      = stringResource(Res.string.already_have_account),
                fontFamily = FontFamily.Default,
                fontSize   = 14.sp,
                color      = CodeNestColors.onSurfaceVariant,
                textDecoration = TextDecoration.None,
                modifier  = Modifier
                    .clickable { onNavigateToLogin() }
                    .padding(4.dp),
            )
        }
    }
}
