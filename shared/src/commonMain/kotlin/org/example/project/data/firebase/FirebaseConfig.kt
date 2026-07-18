package org.example.project.data.firebase

/**
 * Firebase project configuration for CodeNest (codenest-cf49e).
 *
 * Used by the Ktor-based Firebase REST client on all platforms,
 * and by the native SDKs (Android google-services.json, Web JS SDK).
 */
object FirebaseConfig {
    const val apiKey = "AIzaSyBsv8itN4RyTJ_CuxFG_pivrQnfDsSaY7Y"
    const val authDomain = "codenest-cf49e.firebaseapp.com"
    const val projectId = "codenest-cf49e"
    const val storageBucket = "codenest-cf49e.firebasestorage.app"
    const val messagingSenderId = "95095284684"
    const val appId = "1:95095284684:web:c99f15c2c2f1e5fdcf7369"

    /** Base URL for Firebase Auth REST API. */
    const val authBaseUrl = "https://identitytoolkit.googleapis.com/v1"
}
