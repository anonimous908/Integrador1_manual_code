package org.example.project.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextDecoration
import org.jetbrains.compose.resources.painterResource
import kotlinproject.shared.generated.resources.*
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.theme.CodeNestColors
import org.jetbrains.compose.resources.stringResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.hide_password
import kotlinproject.shared.generated.resources.show_password
import kotlinproject.shared.generated.resources.email_divider
private val Inter = FontFamily.Default
private val JetBrainsMono = FontFamily.Monospace

@Composable
fun CodeNestTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text       = label,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize   = 12.sp,
            color      = CodeNestColors.onSurfaceVariant,
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            placeholder   = {
                Text(
                    text       = placeholder,
                    fontFamily = Inter,
                    fontSize   = 14.sp,
                    color      = CodeNestColors.outlineVariant,
                )
            },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape         = RoundedCornerShape(8.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor  = CodeNestColors.InputBackground,
                focusedContainerColor    = CodeNestColors.InputBackground,
                unfocusedBorderColor     = CodeNestColors.outlineVariant,
                focusedBorderColor       = CodeNestColors.primary,
                cursorColor              = CodeNestColors.primary,
                unfocusedTextColor       = CodeNestColors.onSurface,
                focusedTextColor         = CodeNestColors.onSurface,
            ),
            textStyle = TextStyle(
                fontFamily = Inter,
                fontSize   = 14.sp,
            ),
            trailingIcon = trailingIcon,
            isError = error != null,
            supportingText = error?.let {
                { Text(text = it, color = CodeNestColors.error, fontSize = 10.sp) }
            }
        )
    }
}

@Composable
fun PasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisible: () -> Unit,
    error: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text       = label,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize   = 12.sp,
            color      = CodeNestColors.onSurfaceVariant,
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            placeholder   = { Text(text = "••••••••", color = CodeNestColors.outlineVariant) },
            singleLine            = true,
            visualTransformation  = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions       = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon          = {
                IconButton(onClick = onToggleVisible) {
                    Icon(
                        imageVector  = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(if (visible) Res.string.hide_password else Res.string.show_password),
                        tint = CodeNestColors.onSurfaceVariant,
                    )
                }
            },
            shape  = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor  = CodeNestColors.InputBackground,
                focusedContainerColor    = CodeNestColors.InputBackground,
                unfocusedBorderColor     = CodeNestColors.outlineVariant,
                focusedBorderColor       = CodeNestColors.primary,
                cursorColor              = CodeNestColors.primary,
                unfocusedTextColor       = CodeNestColors.onSurface,
                focusedTextColor         = CodeNestColors.onSurface,
            ),
            textStyle = TextStyle(fontFamily = Inter, fontSize = 14.sp),
            isError = error != null,
            supportingText = error?.let {
                { Text(text = it, color = CodeNestColors.error, fontSize = 10.sp) }
            }
        )
    }
}

@Composable
fun EmailDivider(text: String = stringResource(Res.string.email_divider)) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = CodeNestColors.outlineVariant, thickness = 1.dp)
        Text(
            text = text,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = CodeNestColors.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = CodeNestColors.outlineVariant, thickness = 1.dp)
    }
}


// ─── Extracted from CreateAccountScreen ───

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick   = onClick,
        modifier  = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape     = RoundedCornerShape(8.dp),
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

fun buildTermsAnnotatedString(
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
fun CodeNestFooter(
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

// ─── Shared UI Primitives (Anti-Duplication) ──────────────────────────────────

@Composable
fun ErrorBanner(
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    if (errorMessage == null) return
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = CodeNestColors.error.copy(alpha = 0.1f)
    ) {
        Text(
            text = errorMessage,
            color = CodeNestColors.error,
            fontSize = 14.sp,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CodeNestTagChip(
    text: String,
    accent: Color,
    modifier: Modifier = Modifier,
    shapeRadius: Dp = 6.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(shapeRadius))
            .background(accent.copy(alpha = 0.15f))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(shapeRadius))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = accent, fontSize = 10.sp)
    }
}

@Composable
fun DecorativeGlow(glowColor: Color, size: Dp = 600.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor, Color.Transparent)
                ),
                shape = CircleShape
            )
    )
}