package org.example.project.domain.usecase

/**
 * Caso de uso responsable de validar que la contraseña cumpla con las políticas de seguridad.
 */
class ValidatePasswordUseCase {
    /**
     * Evalúa si una contraseña cumple con los requisitos mínimos de seguridad.
     *
     * @param password [String] La contraseña ingresada por el usuario.
     * @return [String]? Retorna `null` si la contraseña es válida. Retorna un [String] con
     * el mensaje de error específico si es demasiado corta, falta mayúscula o falta número.
     * @throws Exception No lanza excepciones.
     *
     * **Ejemplo de uso:**
     * ```kotlin
     * val error = validatePasswordUseCase("debiles")
     * if (error != null) { showPassError(error) }
     * ```
     */
    operator fun invoke(password: String): String? {
        if (password.isBlank()) {
            return "La contraseña no puede estar vacía"
        }
        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            return "La contraseña debe contener al menos una mayúscula"
        }
        if (!password.matches(".*[0-9].*".toRegex())) {
            return "La contraseña debe contener al menos un número"
        }
        return null
    }
}
