package org.example.project.presentation

import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import kotlinproject.shared.generated.resources.*
import org.example.project.presentation.components.EmailDivider
import org.example.project.presentation.components.CodeNestTextField
import org.example.project.presentation.components.PasswordTextField
import org.example.project.presentation.components.Inter
import org.example.project.presentation.components.JetBrainsMono
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.withStyle

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.example.project.presentation.theme.CodeNestColors
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import org.koin.compose.viewmodel.koinViewModel
import org.example.project.presentation.register.RegisterViewModel
import org.example.project.presentation.register.RegisterEvent
import androidx.compose.runtime.collectAsState

// ─── Design Tokens ───────────────────────────────────────────────────────────


private val Manrope     = FontFamily.Default




// ─── Screen wrapper ──────────────────────────────────────────────────────────

class CreateAccountScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: RegisterViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(state.success) {
            if (state.success) {
                navigator.replace(HomeScreen(state.email))
            }
        }

        CreateAccountScreenContent(
            name = state.name,
            onNameChange = { viewModel.onEvent(RegisterEvent.NameChanged(it)) },
            email = state.email,
            onEmailChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
            password = state.pass,
            onPasswordChange = { viewModel.onEvent(RegisterEvent.PassChanged(it)) },
            termsAccepted = state.termsAccepted,
            onTermsChange = { viewModel.onEvent(RegisterEvent.TermsAcceptedChanged(it)) },
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            onCreateAccount = { viewModel.onEvent(RegisterEvent.Submit) },
            onSignInWithGoogle = { },
            onNavigateToLogin = { navigator.pop() },
            onTermsClick = { navigator.push(TermsAndConditionsScreen()) },
            onPrivacyClick = { navigator.push(PrivacyPolicyScreen()) }
        )
    }
}

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
internal fun CreateAccountScreenContent(
    name: String, onNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    termsAccepted: Boolean, onTermsChange: (Boolean) -> Unit,
    isLoading: Boolean, errorMessage: String?,
    onCreateAccount: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CodeNestColors.background)
    ) {
        // ── Decorative glow blob ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.Center)
                .blur(80.dp)
                .background(CodeNestColors.PrimaryGlow, CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Header ───────────────────────────────────────────────────
            CodeNestHeader()

            // ── Auth card ────────────────────────────────────────────────
            Spacer(modifier = Modifier.weight(1f, fill = false))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            AuthCard(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                name            = name,
                onNameChange    = onNameChange,
                email           = email,
                onEmailChange   = onEmailChange,
                password        = password,
                onPasswordChange = onPasswordChange,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible },
                termsAccepted   = termsAccepted,
                onTermsChange   = onTermsChange,
                onSignInWithGoogle = onSignInWithGoogle,
                onCreateAccount = onCreateAccount,
                onNavigateToLogin = onNavigateToLogin,
                onTermsClick    = onTermsClick,
                onPrivacyClick  = onPrivacyClick,
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))

            // ── Footer ───────────────────────────────────────────────────
            CodeNestFooter(
                onTermsClick   = onTermsClick,
                onPrivacyClick = onPrivacyClick,
            )
        }
    }
}

// ─── Header ──────────────────────────────────────────────────────────────────

@Composable
private fun CodeNestHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeNestColors.surfaceContainer)
            .border(
                width = 1.dp,
                color = CodeNestColors.outlineVariant.copy(alpha = 0.30f),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = stringResource(Res.string.app_name),
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize   = 24.sp,
            color      = CodeNestColors.primary,
        )
    }
}

// ─── Auth Card ───────────────────────────────────────────────────────────────

@Composable
private fun AuthCard(
    modifier: Modifier = Modifier,
    name: String, onNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean, onTogglePassword: () -> Unit,
    termsAccepted: Boolean, onTermsChange: (Boolean) -> Unit,
    onSignInWithGoogle: () -> Unit,
    onCreateAccount: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CodeNestColors.GlassPanel)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        CodeNestColors.primary.copy(alpha = 0.20f),
                        CodeNestColors.outlineVariant.copy(alpha = 0.50f),
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(24.dp)
    ) {
        Column(
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
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 32.sp,
                    lineHeight = 40.sp,
                    letterSpacing = (-0.02).sp,
                    color      = CodeNestColors.onSurface,
                    textAlign  = TextAlign.Center,
                )
                Text(
                    text       = stringResource(Res.string.create_account_subtitle),
                    fontFamily = Inter,
                    fontSize   = 14.sp,
                    color      = CodeNestColors.onSurfaceVariant,
                    textAlign  = TextAlign.Center,
                )
            }

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
                onToggleVisible = onTogglePassword,
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
                shape    = RoundedCornerShape(4.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = CodeNestColors.primary,
                    contentColor   = CodeNestColors.onPrimaryContainer,
                    disabledContainerColor = CodeNestColors.primary.copy(alpha = 0.5f),
                ),
                enabled  = termsAccepted,
            ) {
                Text(
                    text       = stringResource(Res.string.create_account_button),
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 12.sp,
                )
            }

            // Already have an account
            Text(
                text      = stringResource(Res.string.already_have_account),
                fontFamily = Inter,
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

// ─── Google Button ────────────────────────────────────────────────────────────

@Composable
private fun GoogleSignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick   = onClick,
        modifier  = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape     = RoundedCornerShape(4.dp),
        border    = BorderStroke(
            1.dp, CodeNestColors.outlineVariant
        ),
        colors    = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor   = CodeNestColors.onSurface,
        ),
    ) {
        // Google "G" logo
        Image(painter = painterResource(Res.drawable.google_logo), contentDescription = "Google Logo", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text       = stringResource(Res.string.register_with_google),
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize   = 12.sp,
        )
    }
}

// ─── Terms Checkbox ───────────────────────────────────────────────────────────

@Composable
fun TermsCheckbox(
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    val part1 = stringResource(Res.string.accept_terms_part1)
    val termsStr = stringResource(Res.string.terms_title)
    val part2 = stringResource(Res.string.accept_terms_part2)
    val privacyStr = stringResource(Res.string.privacy_title)
    val termsText = remember(part1, termsStr, part2, privacyStr) {
        buildTermsAnnotatedString(part1, termsStr, part2, privacyStr)
    }

    Row(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment   = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Checkbox(
            checked         = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            modifier        = Modifier.size(20.dp),
            colors          = CheckboxDefaults.colors(
                checkedColor         = CodeNestColors.primary,
                uncheckedColor       = CodeNestColors.outlineVariant,
                checkmarkColor       = CodeNestColors.onPrimaryContainer,
            ),
        )
        // Inline clickable spans for terms/privacy
        androidx.compose.foundation.text.ClickableText(
            text = termsText,
            style = TextStyle(
                fontFamily = Inter,
                fontSize   = 14.sp,
                color      = CodeNestColors.onSurfaceVariant,
            ),
            onClick = { offset ->
                termsText
                    .getStringAnnotations("TERMS", offset, offset)
                    .firstOrNull()?.let { onTermsClick() }
                termsText
                    .getStringAnnotations("PRIVACY", offset, offset)
                    .firstOrNull()?.let { onPrivacyClick() }
            },
        )
    }
}

private fun buildTermsAnnotatedString(
    part1: String,
    terms: String,
    part2: String,
    privacy: String
): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        append(part1)
        append(" ")

        withStyle(
            style = SpanStyle(
                color = CodeNestColors.primary,
                fontWeight = FontWeight.Medium,
            )
        ) {
            pushStringAnnotation(tag = "TERMS", annotation = "TERMS")
            append(terms)
            pop()
        }

        append(" ")
        append(part2)
        append(" ")

        pushStringAnnotation("PRIVACY", "privacy")
        pushStyle(
            SpanStyle(
                color          = CodeNestColors.primary,
                textDecoration = TextDecoration.Underline,
            )
        )
        append(privacy)
        pop()
        pop()

        append(".")
    }
}

// ─── Footer ───────────────────────────────────────────────────────────────────

@Composable
private fun CodeNestFooter(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text           = "Términos y Condiciones",
                fontFamily     = JetBrainsMono,
                fontWeight     = FontWeight.Medium,
                fontSize       = 12.sp,
                color          = CodeNestColors.onSurfaceVariant,
                textDecoration = TextDecoration.Underline,
                modifier       = Modifier.clickable { onTermsClick() },
            )
            Text(
                text           = "Política de Privacidad",
                fontFamily     = JetBrainsMono,
                fontWeight     = FontWeight.Medium,
                fontSize       = 12.sp,
                color          = CodeNestColors.onSurfaceVariant,
                textDecoration = TextDecoration.Underline,
                modifier       = Modifier.clickable { onPrivacyClick() },
            )
        }
        Text(
            text       = "© 2026 CodeNest. Todos los derechos reservados.",
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize   = 12.sp,
            color      = CodeNestColors.onSurfaceVariant,
        )
    }
}

