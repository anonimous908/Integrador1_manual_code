package org.example.project.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel that provides a [launchSafely] helper to reduce boilerplate
 * for async operations that need loading + error handling.
 *
 * Subclasses hold their own [MutableStateFlow] and pass [setLoading] / [setError]
 * lambdas, keeping this class state-agnostic.
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Launches a coroutine in [viewModelScope] with a [CoroutineExceptionHandler]
     * to avoid uncaught exceptions crashing the app.
     *
     * @param onError   Called with the error message when an uncaught exception occurs.
     * @param setLoading Called before and after the [block] to toggle loading state.
     * @param block     The suspend lambda to execute safely.
     */
    protected fun launchSafely(
        onError: (String) -> Unit = {},
        setLoading: ((Boolean) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            setLoading?.invoke(false)
            onError(throwable.message ?: "Error desconocido")
        }
        viewModelScope.launch(handler) {
            setLoading?.invoke(true)
            block()
            setLoading?.invoke(false)
        }
    }
}
