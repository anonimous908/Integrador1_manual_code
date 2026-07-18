package org.example.project.domain.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.example.project.domain.model.OcrCodeCapture

/**
 * Servicio de dominio reactivo (Clean Architecture) que canaliza en tiempo real
 * las capturas de código enviadas por la app Android con Google ML Kit OCR.
 */
interface LiveOcrReceiverService {
    val incomingOcrCaptures: SharedFlow<OcrCodeCapture>
    suspend fun emitOcrCapture(capture: OcrCodeCapture)
}

/**
 * Hub central (Singleton in-memory bridge) que conecta el receptor de red / Android ML Kit
 * con los ViewModels activos de la interfaz de escritorio.
 */
object LiveOcrReceiverHub : LiveOcrReceiverService {
    private val _incomingOcrCaptures = MutableSharedFlow<OcrCodeCapture>(extraBufferCapacity = 16)
    override val incomingOcrCaptures: SharedFlow<OcrCodeCapture> = _incomingOcrCaptures.asSharedFlow()

    override suspend fun emitOcrCapture(capture: OcrCodeCapture) {
        _incomingOcrCaptures.emit(capture)
    }

    /**
     * Emite de forma sincrónica para adaptadores e hilos externos HTTP / sockets.
     */
    fun tryEmitOcrCapture(capture: OcrCodeCapture): Boolean {
        return _incomingOcrCaptures.tryEmit(capture)
    }
}
