package org.example.project.domain.usecase

class ValidateEmailUseCase {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    
    /**
     * Evalúa si un texto cumple con las reglas de un correo electrónico válido.
     *
     * @param email [String] El texto del correo electrónico a evaluar.
     * @return [String]? Retorna `null` si el correo es válido. Retorna un [String] con el
     * mensaje de error si el correo está vacío o tiene un formato incorrecto.
     * @throws Exception No lanza excepciones.
     *
     * **Ejemplo de uso:**
     * ```kotlin
     * val error = validateEmailUseCase("usuario@mal")
     * if (error != null) { showError(error) }
     * ```
     */
    operator fun invoke(email: String): String? {
        if (email.isBlank()) {
            return "El correo no puede estar vacío"
        }
        if (!email.matches(emailRegex)) {
            return "Formato de correo inválido"
        }
        return null // Null significa "sin error"
    }
}
