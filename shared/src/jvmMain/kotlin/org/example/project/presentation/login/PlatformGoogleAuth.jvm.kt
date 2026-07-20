package org.example.project.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.data.firebase.FirebaseConfig
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.URI

/**
 * Singleton que administra el servidor temporal loopback (puerto 8085) en Escritorio para Google OAuth.
 * Al ser un singleton, actualiza dinámicamente las callbacks de la pantalla actual (Login o Registro),
 * evitando errores de "Address already in use" y asegurando que el token llegue a la pantalla correcta.
 */
private object DesktopOAuthServer {
    private var server: HttpServer? = null
    @Volatile private var activeOnTokenReceived: ((String) -> Unit)? = null
    @Volatile private var activeOnError: ((String) -> Unit)? = null

    /** Parses a query string like "key=val&key2=val2" into a Map. */
    private fun String.parseQuery(): Map<String, String> =
        split("&").mapNotNull { it.split("=").takeIf { p -> p.size == 2 }?.let { p -> p[0] to p[1] } }.toMap()

    @Synchronized
    fun launchAuth(
        port: Int,
        onTokenReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        activeOnTokenReceived = onTokenReceived
        activeOnError = onError

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (server == null) {
                    val newServer = HttpServer.create(InetSocketAddress(port), 0)

                    // /callback: serves HTML + extracts id_token from query if present
                    newServer.createContext("/callback") { exchange ->
                        val params = (exchange.requestURI.query ?: "").parseQuery()
                        params["id_token"]?.let { token ->
                            CoroutineScope(Dispatchers.Main).launch { activeOnTokenReceived?.invoke(token) }
                        }
                        val html = """
                            <!DOCTYPE html><html lang="es"><head><meta charset="UTF-8">
                            <title>CodeNest - Autenticación con Google</title>
                            <style>body{font-family:'Segoe UI',Tahoma,sans-serif;background:#0d1117;color:#e6edf3;display:flex;align-items:center;justify-content:center;height:100vh;margin:0;text-align:center}.card{background:#161b22;padding:40px;border-radius:12px;border:1px solid #30363d;box-shadow:0 8px 24px rgba(0,0,0,.5)}</style>
                            <script>window.onload=function(){const h=window.location.hash.substring(1);const p=new URLSearchParams(h||window.location.search);const t=p.get('id_token');if(t)fetch('/token?id_token='+t).then(()=>{document.getElementById('c').innerHTML='<h2 style="color:#3fb950">✅ Sesión iniciada</h2><p style="color:#8b949e">Ya puedes cerrar esta ventana.</p>';});}</script>
                            </head><body><div class="card" id="c"><h2>Procesando credenciales de Google...</h2></div></body></html>
                        """.trimIndent().toByteArray(Charsets.UTF_8)
                        exchange.sendResponseHeaders(200, html.size.toLong())
                        exchange.responseBody.use { it.write(html) }
                    }

                    // /token: receives id_token via fetch(), fires callback, stops server
                    newServer.createContext("/token") { exchange ->
                        val params = (exchange.requestURI.query ?: "").parseQuery()
                        params["id_token"]?.let { token ->
                            CoroutineScope(Dispatchers.Main).launch { activeOnTokenReceived?.invoke(token) }
                        }
                        val ok = "OK".toByteArray()
                        exchange.sendResponseHeaders(200, ok.size.toLong())
                        exchange.responseBody.use { it.write(ok) }
                        CoroutineScope(Dispatchers.IO).launch {
                            kotlinx.coroutines.delay(2000)
                            stopServer()
                        }
                    }

                    newServer.start()
                    server = newServer
                }

                // Abrimos directamente la URL oficial de Google OAuth 2.0 en el navegador del sistema
                val authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                        "client_id=${FirebaseConfig.webClientId}" +
                        "&redirect_uri=http://localhost:$port/callback" +
                        "&response_type=id_token%20token" +
                        "&scope=openid%20email%20profile" +
                        "&nonce=codenest_desktop"

                Desktop.getDesktop().browse(URI(authUrl))
            } catch (e: Exception) {
                stopServer()
                CoroutineScope(Dispatchers.Main).launch {
                    activeOnError?.invoke("Error en Google Auth para Escritorio: ${e.message}")
                }
            }
        }
    }

    @Synchronized
    fun stopServer() {
        try { server?.stop(0) } catch (_: Exception) {}
        server = null
    }
}

@Composable
actual fun rememberGoogleSignInLauncher(
    onTokenReceived: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return remember(onTokenReceived, onError) {
        {
            DesktopOAuthServer.launchAuth(
                port = 8085,
                onTokenReceived = onTokenReceived,
                onError = onError
            )
        }
    }
}
