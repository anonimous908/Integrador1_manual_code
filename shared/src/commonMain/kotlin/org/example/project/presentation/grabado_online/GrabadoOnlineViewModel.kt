package org.example.project.presentation.grabado_online

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.GrabadoOnlineSession
import org.example.project.domain.service.LiveOcrReceiverHub
import org.example.project.domain.usecase.ToggleGrabadoOnlineUseCase
import androidx.lifecycle.viewModelScope
import org.example.project.presentation.base.BaseViewModel

/**
 * ViewModel que gestiona el estado y la lógica de negocio de Grabado Online (MVVM + Clean Architecture).
 */
class GrabadoOnlineViewModel(
    private val toggleGrabadoOnlineUseCase: ToggleGrabadoOnlineUseCase = ToggleGrabadoOnlineUseCase()
) : BaseViewModel() {

    private val _state = MutableStateFlow(GrabadoOnlineState())
    val state: StateFlow<GrabadoOnlineState> = _state.asStateFlow()

    init {
        org.example.project.data.network.RemoteOcrSyncRelay.startListeningRemoteCaptures(viewModelScope)
        launchSafely {
            LiveOcrReceiverHub.incomingOcrCaptures.collect { ocrCapture ->
                if (_state.value.isRecording) {
                    onEvent(GrabadoOnlineEvent.OnOcrCaptureReceived(
                        codeText = ocrCapture.codeText,
                        sourceDevice = ocrCapture.sourceDevice
                    ))
                }
            }
        }
    }

    fun onEvent(event: GrabadoOnlineEvent) {
        when (event) {
            GrabadoOnlineEvent.ToggleRecording -> {
                _state.update { currentState ->
                    val updatedSession = toggleGrabadoOnlineUseCase(currentState.session)
                    currentState.copy(
                        session = updatedSession,
                        isRecording = updatedSession.isRecording,
                        statusText = if (updatedSession.isRecording) "Grabando en vivo: ON" else "Grabando en vivo"
                    )
                }
            }
            GrabadoOnlineEvent.ResetSession -> {
                _state.update {
                    GrabadoOnlineState(session = GrabadoOnlineSession())
                }
            }
            is GrabadoOnlineEvent.OnOcrCaptureReceived -> {
                _state.update { currentState ->
                    currentState.copy(
                        latestOcrCode = event.codeText,
                        ocrSourceDevice = event.sourceDevice
                    )
                }
            }
        }
    }
}
