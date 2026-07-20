package org.example.project.presentation.login

import androidx.compose.runtime.Composable

/**
 * Hook composable multiplataforma para iniciar el flujo de inicio de sesión con Google.
 * Retorna una función que se invoca al presionar el botón de Google.
 *
 * @param onTokenReceived Callback invocado cuando se obtiene exitosamente el ID Token o token de sesión.
 * @param onError Callback invocado si ocurre un error en la autenticación con Google.
 */
@Composable
expect fun rememberGoogleSignInLauncher(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit
