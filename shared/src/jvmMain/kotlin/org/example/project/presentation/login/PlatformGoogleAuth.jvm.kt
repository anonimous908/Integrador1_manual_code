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

                    newServer.createContext("/callback") { exchange ->
                        val query = exchange.requestURI.query ?: ""
                        
                        val responseHtml = """
                            <!DOCTYPE html>
                            <html lang="es">
                            <head>
                              <meta charset="UTF-8">
                              <title>CodeNest - Autenticación con Google</title>
                              <style>
                                body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #0d1117; color: #e6edf3; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; text-align: center; }
                                .card { background: #161b22; padding: 40px; border-radius: 12px; border: 1px solid #30363d; box-shadow: 0 8px 24px rgba(0,0,0,0.5); }
                              </style>
                              <script>
                                window.onload = function() {
                                  const hash = window.location.hash.substring(1);
                                  const params = new URLSearchParams(hash || window.location.search);
                                  const idToken = params.get('id_token');
                                  
                                  if (idToken) {
                                    fetch('/token?id_token=' + idToken).then(() => {
                                      document.getElementById('content').innerHTML = '<h2 style="color: #3fb950;">✅ Inicio de sesión completado</h2><p style="color: #8b949e;">Has iniciado sesión en CodeNest con tu cuenta de Google.<br>Ya puedes cerrar esta ventana o pestaña y regresar a la aplicación de escritorio.</p>';
                                    });
                                  }
                                };
                              </script>
                            </head>
                            <body>
                              <div class="card" id="content">
                                <h2>Procesando credenciales de Google...</h2>
                                <p style="color: #8b949e;">Por favor espera un segundo mientras conectamos con la aplicación de escritorio.</p>
                              </div>
                            </body>
                            </html>
                        """.trimIndent()

                        val params = query.split("&").mapNotNull {
                            val parts = it.split("=")
                            if (parts.size == 2) parts[0] to parts[1] else null
                        }.toMap()

                        val directToken = params["id_token"]
                        if (directToken != null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                activeOnTokenReceived?.invoke(directToken)
                            }
                        }

                        val bytes = responseHtml.toByteArray(Charsets.UTF_8)
                        exchange.sendResponseHeaders(200, bytes.size.toLong())
                        exchange.responseBody.use { it.write(bytes) }
                    }

                    newServer.createContext("/token") { exchange ->
                        val query = exchange.requestURI.query ?: ""
                        val params = query.split("&").mapNotNull {
                            val parts = it.split("=")
                            if (parts.size == 2) parts[0] to parts[1] else null
                        }.toMap()

                        val idToken = params["id_token"]
                        if (idToken != null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                activeOnTokenReceived?.invoke(idToken)
                            }
                        }

                        val bytes = "OK".toByteArray()
                        exchange.sendResponseHeaders(200, bytes.size.toLong())
                        exchange.responseBody.use { it.write(bytes) }

                        // Detenemos el servidor una vez autenticado después de un breve retraso
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
        try {
            server?.stop(0)
        } catch (_: Exception) {}
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
