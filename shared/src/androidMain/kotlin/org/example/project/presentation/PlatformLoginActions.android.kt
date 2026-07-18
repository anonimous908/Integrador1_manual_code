package org.example.project.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.grabado_online.AndroidGrabadoOnlineVoyagerScreen

@Composable
actual fun PlatformLoginActions(navigator: Navigator) {
    Spacer(modifier = Modifier.height(14.dp))
    Button(
        onClick = { navigator.push(AndroidGrabadoOnlineVoyagerScreen()) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "📷 [ GRABAR ONLINE EN VIVO (OCR) ]",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}
