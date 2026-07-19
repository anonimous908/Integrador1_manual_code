package org.example.project.presentation.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.example.project.data.firebase.FirebaseConfig

@Composable
actual fun rememberGoogleSignInLauncher(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current

    // Launcher nativo de ActivityResult para recibir la respuesta de Google Play Services en Android
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    onTokenReceived(idToken)
                } else {
                    onError("El ID Token de Google no se recibió. Verifica el Web Client ID en Firebase.")
                }
            } catch (e: ApiException) {
                onError("Error nativo Google Sign-In (código ${e.statusCode}): ${e.message}")
            }
        } else {
            // El usuario cerró o canceló el diálogo nativo
        }
    }

    return remember(context, launcher) {
        {
            try {
                val activity = context as? Activity
                if (activity != null) {
                    // Configuración oficial para solicitar el ID Token al servidor de Google con tu Web Client ID
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(FirebaseConfig.webClientId)
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(activity, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                } else {
                    onError("No se pudo obtener la Activity actual de Android.")
                }
            } catch (e: Exception) {
                onError("Error al iniciar Google Auth nativo: ${e.message}")
            }
        }
    }
}
