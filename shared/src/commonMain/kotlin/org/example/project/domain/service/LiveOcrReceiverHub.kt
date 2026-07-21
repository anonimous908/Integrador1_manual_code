package org.example.project.domain.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.example.project.domain.model.OcrCodeCapture

/**
 * Hub central (Singleton in-memory bridge) que conecta el receptor de red / Android ML Kit
 * con los ViewModels activos de la interfaz de escritorio y móvil.
 */
object LiveOcrReceiverHub {
    private val _incomingOcrCaptures = MutableSharedFlow<OcrCodeCapture>(extraBufferCapacity = 16)
    val incomingOcrCaptures: SharedFlow<OcrCodeCapture> = _incomingOcrCaptures.asSharedFlow()

    suspend fun emitOcrCapture(capture: OcrCodeCapture) {
        _incomingOcrCaptures.emit(capture)
    }

    /**
     * Emite de forma sincrónica para adaptadores e hilos externos HTTP / sockets.
     */
    fun tryEmitOcrCapture(capture: OcrCodeCapture): Boolean {
        return _incomingOcrCaptures.tryEmit(capture)
    }
}
