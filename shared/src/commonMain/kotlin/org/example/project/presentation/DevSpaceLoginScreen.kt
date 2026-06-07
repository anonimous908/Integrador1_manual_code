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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinproject.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import kotlinproject.shared.generated.resources.Bison_Logo_Studios
import org.example.project.presentation.theme.DevSpaceLoginBrand
import org.example.project.presentation.theme.DevSpaceLoginColors

/**
 * Calcula el padding para el brand fijo en la esquina inferior derecha.
 * Garantiza un mínimo de 16dp, escalando proporcionalmente al tamaño
 * del viewport disponible.
 *
 * @param totalSize Dimensión (maxHeight o maxWidth) del contenedor padre.
 * @return Padding calculado, clampado a mínimo 16dp.
 */
internal fun calculateBrandPadding(totalSize: Dp): Dp = maxOf(16.dp, totalSize * 0.03f)

@Composable
fun DevSpaceLoginScreen() {
    // Estados del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    /* BoxWithConstraints:
     * En lugar de usar Box + onSizeChanged + invalidar manual,
     * BoxWithConstraints ya expone maxWidth/maxHeight del viewport
     * disponible SIN recomposiciones adicionales. Más eficiente
     * y más declarativo.
     */
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(DevSpaceLoginColors.background)
    ) {
        /* Factor de escala responsivo:
         *   scaleFactor = anchoDisponible / 480.dp (ancho de referencia)
         *
         * 480.dp es el ancho para el que diseñamos. En una ventana de
         * 320dp → scaleFactor ~0.67 (todo se reduce).
         * En 1920dp → se clampa a 1.5 (máximo).
         *
         * coerceIn(0.5f, 1.5f) evita que elementos sean demasiado
         * pequeños en pantallas miniatura o enormes en monitores ultra-wide.
         *
         * Todos los paddings, spacings, iconos y sizes internos se
         * multiplican por scaleFactor para escalar proporcionalmente.
         */
        val scaleFactor = (maxWidth / 480.dp).coerceIn(0.5f, 1.5f)

        /* Glow ambiental: tamaño basado en el lado MÁS CHICO del viewport.
         *   glowSize = minOf(maxWidth, maxHeight) * 0.7f
         *
         * Elegimos minOf en vez de maxWidth SOLO porque el glow es un
         * círculo — no tiene sentido que sea más grande que la dimensión
         * más corta de la pantalla.
         *
         * 0.7f = 70% del lado más chico, para que no ahogue el contenido.
         * coerceIn(200.dp, 800.dp) evita que desaparezca en móvil o
         * explote en ultra-wide.
         */
        val glowSize = (minOf(maxWidth, maxHeight) * 0.7f).coerceIn(200.dp, 800.dp)

        // ========================================================
        // GLOW AMBIENTAL — efecto de fondo radial
        // ========================================================
        Box(
            modifier = Modifier
                .size(glowSize)                      // ← tamaño responsivo
                .align(Alignment.Center)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DevSpaceLoginColors.primaryContainer.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // ========================================================
        // CARD PRINCIPAL — Column scrolleable con todo el contenido
        // ========================================================
        /* Column único con verticalScroll + Arrangement.Center:
          *
          * - Cuando el contenido entra en el viewport → Arrangement.Center
          *   lo centra verticalmente (footer y contenido).
          * - Cuando NO entra → scroll natural, sin superposición.
          * - El brand (logo + "Ztrene Studios") está fuera de este Column,
          *   fijo en la esquina inferior derecha del BoxWithConstraints.
          * - Un protection Spacer al final evita superposición con el brand.
          *
          * NOTA: No usamos Spacer(Modifier.weight()) porque weight NO
          * funciona dentro de un Column con verticalScroll — el scrollable
          * no tiene una altura fija para distribuir. Arrangement.Center
          * logra un efecto similar sin romperse en scroll.
          */
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = (maxWidth * 0.85f).coerceIn(320.dp, 500.dp))  // ← 85% del ancho, clamped entre 320 y 500dp
                .padding(horizontal = 16.dp * scaleFactor)
                // Efecto Glassmorphism básico
                .background(
                    color = DevSpaceLoginColors.surfaceContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = DevSpaceLoginColors.outlineVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .verticalScroll(rememberScrollState())  // ← scrolleable si no entra
                .padding(12.dp * scaleFactor),           // ← padding interno proporcional
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center      // ← centra cuando entra
        ) {
            // ---- Header (Icono Terminal) ----
            Box(
                modifier = Modifier
                    .size(48.dp * scaleFactor)
                    .background(DevSpaceLoginColors.surfaceContainerHigh, RoundedCornerShape(8.dp))
                    .border(1.dp, DevSpaceLoginColors.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Terminal Icon",
                    tint = DevSpaceLoginColors.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp * scaleFactor))

            Text(
                text = "DevSpace",
                color = DevSpaceLoginColors.onSurface,
                fontSize = 32.sp,                  // ← font SÍ queda fijo (sp escala con preferencias del usuario)
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp * scaleFactor))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = DevSpaceLoginColors.onSurfaceVariant)) {
                        append("En donde la mente ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = DevSpaceLoginColors.onSurfaceVariant)) {
                        append("crea")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = DevSpaceLoginColors.onSurfaceVariant)) {
                        append(" el programa ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = DevSpaceLoginColors.onSurfaceVariant)) {
                        append("guarda")
                    }
                },
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp * scaleFactor))

            // ---- Botón de Google (OAuth) ----
            OutlinedButton(
                onClick = { /* TODO: Google Login */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DevSpaceLoginColors.surfaceContainerHigh,
                    contentColor = DevSpaceLoginColors.onSurface
                ),
                border = BorderStroke(1.dp, DevSpaceLoginColors.outlineVariant.copy(alpha = 0.5f))
            ) {
                // Aquí idealmente usarías un painterResource para el logo de Google SVG
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp * scaleFactor)
                )
                Spacer(modifier = Modifier.width(8.dp * scaleFactor))
                Text("Iniciar sesión con Google", fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp * scaleFactor))

            // ---- Divider "o continúa con correo electrónico" ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = DevSpaceLoginColors.outlineVariant.copy(alpha = 0.4f))
                Text(
                    text = "o continúa con correo electrónico",
                    color = DevSpaceLoginColors.onSurfaceVariant,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp * scaleFactor)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = DevSpaceLoginColors.outlineVariant.copy(alpha = 0.4f))
            }

            Spacer(modifier = Modifier.height(8.dp * scaleFactor))
 
            // ---- Formulario - Email ----
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Correo electrónico", color = DevSpaceLoginColors.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp * scaleFactor))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("dev@ejemplo.com", color = DevSpaceLoginColors.outline) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DevSpaceLoginColors.surfaceContainerLow,
                        unfocusedContainerColor = DevSpaceLoginColors.surfaceContainerLow,
                        focusedBorderColor = DevSpaceLoginColors.primary,
                        unfocusedBorderColor = DevSpaceLoginColors.outlineVariant.copy(alpha = 0.5f),
                        focusedTextColor = DevSpaceLoginColors.onSurface,
                        unfocusedTextColor = DevSpaceLoginColors.onSurface
                    ),
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = DevSpaceLoginColors.outline, modifier = Modifier.size(18.dp * scaleFactor))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp * scaleFactor))

            // ---- Formulario - Contraseña ----
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Contraseña", color = DevSpaceLoginColors.onSurfaceVariant, fontSize = 12.sp)
                    Text(
                        text = "¿Has olvidado tu contraseña?",
                        color = DevSpaceLoginColors.primary,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                }
                Spacer(modifier = Modifier.height(4.dp * scaleFactor))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", color = DevSpaceLoginColors.outline) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DevSpaceLoginColors.surfaceContainerLow,
                        unfocusedContainerColor = DevSpaceLoginColors.surfaceContainerLow,
                        focusedBorderColor = DevSpaceLoginColors.primary,
                        unfocusedBorderColor = DevSpaceLoginColors.outlineVariant.copy(alpha = 0.5f),
                        focusedTextColor = DevSpaceLoginColors.onSurface,
                        unfocusedTextColor = DevSpaceLoginColors.onSurface
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle password visibility",
                            tint = DevSpaceLoginColors.outline,
                            modifier = Modifier
                                .size(18.dp * scaleFactor)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp * scaleFactor))

            // ---- Botón Iniciar Sesión ----
            Button(
                onClick = { /* TODO: Submit Login */ },
                modifier = Modifier.fillMaxWidth().height(48.dp * scaleFactor),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DevSpaceLoginColors.primaryContainer,
                    contentColor = DevSpaceLoginColors.onPrimaryContainer
                )
            ) {
                Text("Iniciar sesión", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            /* ========================================================
             * FOOTER — dentro del flujo del Column
             *
             * El brand (logo + "Ztrene Studios") se movió fuera de este
             * Column a un Box con Alignment.BottomEnd sobre el
             * BoxWithConstraints raíz. El protection Spacer al final
             * evita que el contenido scrollable quede detrás del brand.
             * ======================================================== */

            Spacer(modifier = Modifier.height(16.dp * scaleFactor))

            // ---- Footer: Registro, Términos y Privacidad ----
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
                    Text("¿No tienes una cuenta? ", color = DevSpaceLoginColors.onSurfaceVariant, fontSize = 13.sp)
                    Text(
                        text = "Regístrate",
                        color = DevSpaceLoginColors.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp * scaleFactor))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Términos y Condiciones",
                        color = DevSpaceLoginColors.onSurfaceVariant,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                    Text(
                        text = " | ",
                        color = DevSpaceLoginColors.onSurfaceVariant.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Política de Privacidad",
                        color = DevSpaceLoginColors.onSurfaceVariant,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                }
            }

            // ---- Protection Spacer: evita que el contenido quede detrás del brand fijo ----
            Spacer(modifier = Modifier.height(80.dp * scaleFactor))
        }

        /* ========================================================
         * BRAND — fijo en la esquina inferior derecha, fuera del scroll
         *
         * Se posiciona con Alignment.BottomEnd sobre el BoxWithConstraints
         * raíz, con padding responsivo que escala con el viewport.
         * ======================================================== */
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = calculateBrandPadding(maxHeight),
                    end = calculateBrandPadding(maxWidth)
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
                    color = DevSpaceLoginColors.onSurfaceVariant,
                    fontSize = (DevSpaceLoginBrand.textSize * scaleFactor)
                        .coerceIn(DevSpaceLoginBrand.textMin, DevSpaceLoginBrand.textMax).sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "version 1.0",
                    color = DevSpaceLoginColors.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = (10 * scaleFactor).sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
