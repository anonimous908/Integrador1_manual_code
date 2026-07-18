package org.example.project.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.login.LoginEvent
import org.example.project.presentation.login.LoginState
import org.example.project.presentation.login.LoginViewModel
import org.example.project.presentation.theme.CodeNestColors
import org.jetbrains.compose.resources.stringResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.email_label
import kotlinproject.shared.generated.resources.email_placeholder
import kotlinproject.shared.generated.resources.forgot_password
import kotlinproject.shared.generated.resources.login_button
import kotlinproject.shared.generated.resources.password_label

internal fun calculateBrandPadding(totalSize: Dp): Dp = maxOf(16.dp, totalSize * 0.03f)

@Composable
fun CodeNestLoginForm(
    viewModel: LoginViewModel,
    scaleFactor: Float,
    state: LoginState
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (state.errorMessage != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = CodeNestColors.error.copy(alpha = 0.1f)
            ) {
                Text(
                    text = state.errorMessage ?: "",
                    color = CodeNestColors.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
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
