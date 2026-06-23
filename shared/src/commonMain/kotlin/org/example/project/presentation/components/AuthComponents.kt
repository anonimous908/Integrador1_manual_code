package org.example.project.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.example.project.presentation.theme.DevSpaceColors
import org.jetbrains.compose.resources.stringResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.hide_password
import kotlinproject.shared.generated.resources.show_password
import kotlinproject.shared.generated.resources.email_divider

val Inter = FontFamily.Default
val JetBrainsMono = FontFamily.Monospace

@Composable
fun DevSpaceTextField(
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
            color      = DevSpaceColors.onSurfaceVariant,
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
                    color      = DevSpaceColors.outlineVariant,
                )
            },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape         = RoundedCornerShape(4.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor  = DevSpaceColors.InputBackground,
                focusedContainerColor    = DevSpaceColors.InputBackground,
                unfocusedBorderColor     = DevSpaceColors.outlineVariant,
                focusedBorderColor       = DevSpaceColors.primary,
                cursorColor              = DevSpaceColors.primary,
                unfocusedTextColor       = DevSpaceColors.onSurface,
                focusedTextColor         = DevSpaceColors.onSurface,
            ),
            textStyle = TextStyle(
                fontFamily = Inter,
                fontSize   = 14.sp,
            ),
            trailingIcon = trailingIcon,
            isError = error != null,
            supportingText = error?.let {
                { Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 10.sp) }
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
            color      = DevSpaceColors.onSurfaceVariant,
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxWidth(),
            placeholder   = { Text(text = "••••••••", color = DevSpaceColors.outlineVariant) },
            singleLine            = true,
            visualTransformation  = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions       = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon          = {
                IconButton(onClick = onToggleVisible) {
                    Icon(
                        imageVector  = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(if (visible) Res.string.hide_password else Res.string.show_password),
                        tint = DevSpaceColors.onSurfaceVariant,
                    )
                }
            },
            shape  = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor  = DevSpaceColors.InputBackground,
                focusedContainerColor    = DevSpaceColors.InputBackground,
                unfocusedBorderColor     = DevSpaceColors.outlineVariant,
                focusedBorderColor       = DevSpaceColors.primary,
                cursorColor              = DevSpaceColors.primary,
                unfocusedTextColor       = DevSpaceColors.onSurface,
                focusedTextColor         = DevSpaceColors.onSurface,
            ),
            textStyle = TextStyle(fontFamily = Inter, fontSize = 14.sp),
            isError = error != null,
            supportingText = error?.let {
                { Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 10.sp) }
            }
        )
    }
}

@Composable
fun EmailDivider(text: String = stringResource(Res.string.email_divider)) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = DevSpaceColors.outlineVariant, thickness = 1.dp)
        Text(
            text = text,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = DevSpaceColors.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = DevSpaceColors.outlineVariant, thickness = 1.dp)
    }
}
