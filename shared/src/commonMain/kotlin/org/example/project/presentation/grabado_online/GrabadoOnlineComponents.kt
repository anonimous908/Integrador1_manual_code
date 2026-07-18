package org.example.project.presentation.grabado_online

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.presentation.theme.CodeNestColors

/**
 * Componente modular para el botón de Grabado en Vivo en el HeaderBar.
 */
@Composable
fun GrabadoOnlineButton(
    state: GrabadoOnlineState,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (state.isRecording) Color(0xFFE53935) else CodeNestColors.surfaceContainerHighest,
            contentColor = if (state.isRecording) Color.White else CodeNestColors.onSurface
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            1.dp,
            if (state.isRecording) Color(0xFFE53935) else CodeNestColors.outlineVariant
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "Grabando en vivo",
            modifier = Modifier.size(14.dp),
            tint = if (state.isRecording) Color.White else Color(0xFFE53935)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = state.statusText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Banner de sincronización móvil (Cámara -> OCR -> Editor) que aparece
 * cuando la grabación en vivo está activa.
 */
@Composable
fun OcrSyncBanner(
    latestOcrCode: String? = null,
    onReceiveOcrCapture: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
            .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FiberManualRecord,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = if (!latestOcrCode.isNullOrEmpty()) "🟢 Código real detectado desde Celular Android" else "🟢 Sincronizado en vivo con tu cámara de Android",
                color = CodeNestColors.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (!latestOcrCode.isNullOrEmpty()) {
            Button(
                onClick = { onReceiveOcrCapture(latestOcrCode) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.FiberManualRecord, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(6.dp))
                Text("Insertar Captura Real", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

