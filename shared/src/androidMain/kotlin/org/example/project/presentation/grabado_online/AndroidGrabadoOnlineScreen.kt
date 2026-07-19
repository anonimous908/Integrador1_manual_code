package org.example.project.presentation.grabado_online

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.project.data.network.AndroidOcrClientBridge
import org.example.project.domain.usecase.CheckOcrModificationsUseCase
import org.example.project.presentation.theme.CodeNestColors

data class OcrScanLogEntry(
    val timestamp: String,
    val hasModifications: Boolean,
    val message: String,
    val extractedTextSnippet: String
)

/**
 * Pantalla de Grabado Online para móvil Android 100% REAL (sin simulación).
 * Muestra vista de cámara real, cuenta regresiva descendente {9,8,7...}
 * y comparación diff inteligente vs archivo.txt.
 */
@Composable
fun AndroidGrabadoOnlineScreen(
    onBackToLogin: () -> Unit
) {
    val checkModificationsUseCase = remember { CheckOcrModificationsUseCase() }
    val ocrBridge = remember { AndroidOcrClientBridge() }

    val deepSeekClient = remember { org.example.project.data.network.DeepSeekV4FlashClient() }
    val coroutineScope = rememberCoroutineScope()
    var currentAiJob by remember { mutableStateOf<Job?>(null) }

    var hasCameraPermission by remember { mutableStateOf(true) }
    var isScanningActive by remember { mutableStateOf(true) }
    var latestLiveOcrText by remember { mutableStateOf("") }
    var recordedFileText by remember { mutableStateOf("") }
    val logs = remember { mutableStateListOf<OcrScanLogEntry>() }

    val launchAiAnalysis: (String) -> Unit = { ocrTextToAnalyze ->
        currentAiJob?.cancel()
        currentAiJob = coroutineScope.launch {
            deepSeekClient.analyzeOcrTextStream(ocrTextToAnalyze).collect { streamingProgress ->
                if (logs.isNotEmpty()) {
                    logs[0] = logs[0].copy(message = streamingProgress)
                }
            }
        }
    }

    // Escaneo continuo en tiempo real y transmisión OCR sin cuenta regresiva
    LaunchedEffect(isScanningActive) {
        while (isScanningActive) {
            delay(2500)
            val currentOcr = latestLiveOcrText.trim()
            val result = checkModificationsUseCase(
                newOcrText = currentOcr,
                lastKnownFileText = recordedFileText
            )
            if (result.hasModifications && currentOcr.isNotEmpty()) {
                recordedFileText = currentOcr
                org.example.project.domain.service.LiveOcrReceiverHub.tryEmitOcrCapture(
                    org.example.project.domain.model.OcrCodeCapture(
                        codeText = currentOcr,
                        sourceDevice = "Android ML Kit Camera"
                    )
                )
                coroutineScope.launch {
                    org.example.project.data.network.RemoteOcrSyncRelay.sendOcrCapture(currentOcr)
                }
                logs.add(
                    0,
                    OcrScanLogEntry(
                        timestamp = "Escaneo Automático",
                        hasModifications = true,
                        message = "Analizando en vivo con DeepSeek v4 Flash (Streaming)...",
                        extractedTextSnippet = currentOcr.take(60) + if (currentOcr.length > 60) "..." else ""
                    )
                )
                launchAiAnalysis(currentOcr)
            } else {
                logs.add(
                    0,
                    OcrScanLogEntry(
                        timestamp = "Escaneo Automático",
                        hasModifications = false,
                        message = result.reason,
                        extractedTextSnippet = if (currentOcr.isEmpty()) "Sin texto detectado en cámara" else currentOcr.take(40) + "..."
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodeNestColors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "📱 Grabación Online Android (Real)",
                    color = CodeNestColors.onSurface,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cámara en vivo + OCR Texto Puro",
                    color = CodeNestColors.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                onClick = onBackToLogin,
                colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.surfaceVariant)
            ) {
                Text("Volver al Login", color = CodeNestColors.onSurface)
            }
        }

        Spacer(Modifier.height(16.dp))

        if (!hasCameraPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF263238)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFFB300))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "🔒 Permiso de Cámara Requerido (android.permission.CAMERA)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Para activar la cámara posterior de Android y procesar texto OCR en la pizarra, concede acceso a la cámara.",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { hasCameraPermission = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Otorgar Permiso de Cámara", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        } else {
            RealCameraPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                onOcrTextDetected = { detectedText ->
                    latestLiveOcrText = detectedText
                }
            )

            Spacer(Modifier.height(16.dp))

            // TARJETA 2: CUENTA REGRESIVA {9, 8, 7, 6...} SEG EN TIEMPO REAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CodeNestColors.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(if (isScanningActive) Color(0xFFFF5252) else Color.Gray)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isScanningActive) "GRABANDO EN VIVO (TIEMPO REAL)" else "GRABACIÓN PAUSADA",
                            color = if (isScanningActive) Color(0xFFFF5252) else CodeNestColors.onSurfaceVariant,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { isScanningActive = !isScanningActive },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isScanningActive) Color(0xFFFFB300) else Color(0xFF4CAF50)
                            )
                        ) {
                            Icon(
                                imageVector = if (isScanningActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (isScanningActive) "Pausar" else "Reanudar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                         Button(
                            onClick = {
                                val currentOcr = latestLiveOcrText.trim()
                                if (currentOcr.isNotEmpty()) {
                                    recordedFileText = currentOcr
                                    org.example.project.domain.service.LiveOcrReceiverHub.tryEmitOcrCapture(
                                        org.example.project.domain.model.OcrCodeCapture(
                                            codeText = currentOcr,
                                            sourceDevice = "Android ML Kit Camera"
                                        )
                                    )
                                    coroutineScope.launch {
                                        org.example.project.data.network.RemoteOcrSyncRelay.sendOcrCapture(currentOcr)
                                    }
                                    logs.add(
                                        0,
                                        OcrScanLogEntry(
                                            timestamp = "Captura Ahora",
                                            hasModifications = true,
                                            message = "Analizando en vivo con DeepSeek v4 Flash (Streaming)...",
                                            extractedTextSnippet = currentOcr.take(60) + if (currentOcr.length > 60) "..." else ""
                                        )
                                    )
                                    launchAiAnalysis(currentOcr)
                                } else {
                                    logs.add(
                                        0,
                                        OcrScanLogEntry(
                                            timestamp = "Captura Ahora",
                                            hasModifications = false,
                                            message = "Captura vacía o sin texto detectado en cámara.",
                                            extractedTextSnippet = "Sin texto detectado"
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = CodeNestColors.primary)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Capturar Ahora", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CodeNestColors.surface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📄 Contenido actual guardado en archivo.txt:",
                    color = CodeNestColors.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (recordedFileText.isEmpty()) "(Aún sin código capturado en archivo.txt)" else recordedFileText,
                    color = CodeNestColors.onSurface,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "📋 Historial de Inspecciones vs archivo.txt (Deduplicación)",
            color = CodeNestColors.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (logs.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CodeNestColors.surface)
                ) {
                    Text(
                        text = "Escaneando código en tiempo real desde la cámara...",
                        color = CodeNestColors.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            } else {
                logs.forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (log.hasModifications) Color(0xFF4CAF50).copy(alpha = 0.15f) else CodeNestColors.surface)
                            .border(
                                width = 1.dp,
                                color = if (log.hasModifications) Color(0xFF4CAF50).copy(alpha = 0.5f) else CodeNestColors.outlineVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "[${log.timestamp}]",
                                    color = CodeNestColors.primary,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = log.message,
                                    color = CodeNestColors.onSurface,
                                    fontSize = 13.sp,
                                    fontWeight = if (log.hasModifications) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            Text(
                                text = log.extractedTextSnippet,
                                color = CodeNestColors.onSurfaceVariant,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

class AndroidGrabadoOnlineVoyagerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AndroidGrabadoOnlineScreen(onBackToLogin = { navigator.pop() })
    }
}
