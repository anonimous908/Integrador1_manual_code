package org.example.project.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.project.data.firebase.FirebaseConfig

@Composable
actual fun rememberGoogleSignInLauncher(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return remember {
        {
            try {
                runJsGoogleAuth(
                    FirebaseConfig.apiKey,
                    FirebaseConfig.authDomain,
                    FirebaseConfig.projectId,
                    FirebaseConfig.appId
                )
            } catch (e: Exception) {
                onError(e.message ?: "Error JS Google Auth")
            }
        }
    }
}

private fun runJsGoogleAuth(apiKey: String, authDomain: String, projectId: String, appId: String) {
    js("""
        const script = document.createElement('script');
        script.type = 'module';
        script.innerHTML = 'import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-app.js"; import { getAuth, signInWithPopup, GoogleAuthProvider } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-auth.js"; const app = initializeApp({ apiKey: "' + apiKey + '", authDomain: "' + authDomain + '", projectId: "' + projectId + '", appId: "' + appId + '" }, "auth_temp_js"); const auth = getAuth(app); signInWithPopup(auth, new GoogleAuthProvider()).then(res => res.user.getIdToken()).then(token => { window.dispatchEvent(new CustomEvent("google_auth_success", { detail: token })); }).catch(e => { if (e.code !== "auth/popup-closed-by-user" && e.code !== "auth/cancelled-popup-request") { console.warn("Google Auth cancelado:", e.message); } });';
        document.head.appendChild(script);
    """)
}
