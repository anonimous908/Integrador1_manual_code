package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke

@Composable
fun DevSpaceLoginScreen() {
    // Definición de colores basados en tu tailwind.config
    val backgroundColor = Color(0xFF131313)
    val primaryColor = Color(0xFF98cbff)
    val primaryContainer = Color(0xFF00a3ff)
    val onPrimaryContainer = Color(0xFF00375a)
    val surfaceContainer = Color(0xFF201f1f)
    val surfaceContainerHigh = Color(0xFF2a2a2a)
    val surfaceContainerLow = Color(0xFF1c1b1b)
    val onSurface = Color(0xFFe5e2e1)
    val onSurfaceVariant = Color(0xFFbec7d4)
    val outlineColor = Color(0xFF88919d)
    val outlineVariant = Color(0xFF3f4852)

    // Estados del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Glow ambiental de fondo (Efecto Radial Gradient en lugar de blur para mejor rendimiento en KMP)
        Box(
            modifier = Modifier
                .size(600.dp)
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryContainer.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Main Login Card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 420.dp)
                .padding(horizontal = 16.dp)
                // Efecto Glassmorphism básico
                .background(
                    color = surfaceContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = outlineVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(surfaceContainerHigh, RoundedCornerShape(8.dp))
                    .border(1.dp, outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Terminal Icon",
                    tint = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "DevSpace",
                color = onSurface,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Bienvenido de nuevo, desarrollador",
                color = onSurfaceVariant,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón de Google (OAuth)
            OutlinedButton(
                onClick = { /* TODO: Google Login */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = surfaceContainerHigh,
                    contentColor = onSurface
                ),
                border = BorderStroke(1.dp, outlineVariant.copy(alpha = 0.5f))
            ) {
                // Aquí idealmente usarías un painterResource para el logo de Google SVG
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar sesión con Google", fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider "o continúa con correo electrónico"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = outlineVariant.copy(alpha = 0.4f))
                Text(
                    text = "o continúa con correo electrónico",
                    color = onSurfaceVariant,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = outlineVariant.copy(alpha = 0.4f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario - Email
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Correo electrónico", color = onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("dev@ejemplo.com", color = outlineColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = surfaceContainerLow,
                        unfocusedContainerColor = surfaceContainerLow,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = outlineVariant.copy(alpha = 0.5f),
                        focusedTextColor = onSurface,
                        unfocusedTextColor = onSurface
                    ),
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = outlineColor, modifier = Modifier.size(18.dp))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario - Contraseña
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Contraseña", color = onSurfaceVariant, fontSize = 12.sp)
                    Text(
                        text = "¿Has olvidado tu contraseña?",
                        color = primaryColor,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", color = outlineColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = surfaceContainerLow,
                        unfocusedContainerColor = surfaceContainerLow,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = outlineVariant.copy(alpha = 0.5f),
                        focusedTextColor = onSurface,
                        unfocusedTextColor = onSurface
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle password visibility",
                            tint = outlineColor,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Iniciar Sesión
            Button(
                onClick = { /* TODO: Submit Login */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryContainer,
                    contentColor = onPrimaryContainer
                )
            ) {
                Text("Iniciar sesión", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer de la Card (Registro)
            Row {
                Text("¿No tienes una cuenta? ", color = onSurfaceVariant, fontSize = 14.sp)
                Text(
                    text = "Regístrate",
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }
        }

        // Footer Principal (Términos, Privacidad)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(
                    text = "Términos y Condiciones",
                    color = onSurfaceVariant,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO */ }
                )
                Text(
                    text = "Política de Privacidad",
                    color = onSurfaceVariant,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ztrene Studios",
                    color = onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
        }
    }
}
