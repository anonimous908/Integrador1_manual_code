package org.example.project.domain.model

import kotlin.uuid.Uuid

data class CodeLine(val number: Int, val content: String)

data class SnippetDetail(
    val breadcrumb: List<String> = listOf("Mis Recetas", "Python", "Interfaz Gráfica"),
    val title: String = "Botón con Tkinter",
    val tags: List<String> = listOf("#Python", "#Tkinter", "#GUI"),
    val createdDate: String = "24 oct. 2023",
    val usageCount: Int = 142,
    val fileName: String = "main.py",
    val description: String = "Una implementación básica de una interfaz gráfica usando " +
        "Tkinter en Python, que incluye un botón funcional y una etiqueta de estado.",
    val dependencies: String = "Python (3.x)",
    val evidenceText: String = "Captura de pantalla de la aplicación Tkinter ejecutándose, mostrando el botón y el mensaje de estado tras ser presionado.",
    val code: List<CodeLine>
) {
    val rawCode: String get() = code.joinToString("\n") { it.content }

    fun withRawCode(newCode: String): SnippetDetail {
        val newLines = newCode.split("\n").mapIndexed { index, s -> CodeLine(index + 1, s) }
        return copy(code = newLines)
    }

    fun toRecipe(userId: String, recipeId: String? = null): Recipe {
        val tag1 = tags.getOrNull(0)?.removePrefix("#")?.trim() ?: ""
        val tag2 = tags.getOrNull(1)?.removePrefix("#")?.trim()
        return Recipe(
            id = recipeId ?: Uuid.random().toString(),
            userId = userId,
            title = title,
            languageTag = tag1,
            secondaryTag = if (tag2.isNullOrBlank()) null else tag2,
            code = code.map { it.content },
            description = description,
            dependencies = listOfNotNull(dependencies.ifBlank { null }),
            evidence = evidenceText,
            createdAt = 0L,
            updatedAt = 0L
        )
    }

    companion object {
        private val sampleCode = listOf(
            CodeLine(1, "import tkinter as tk"),
            CodeLine(2, ""),
            CodeLine(3, "def al_hacer_clic():"),
            CodeLine(4, "    print(\"¡Botón presionado!\")"),
            CodeLine(5, "    etiqueta.config(text=\"Botón fue presionado\")"),
            CodeLine(6, ""),
            CodeLine(7, "# Crear ventana"),
            CodeLine(8, "ventana = tk.Tk()"),
            CodeLine(9, "ventana.title(\"Mi aplicación\")"),
            CodeLine(10, "ventana.geometry(\"300x200\")"),
            CodeLine(11, ""),
            CodeLine(12, "# Crear botón"),
            CodeLine(13, "boton = tk.Button("),
            CodeLine(14, "    ventana,"),
            CodeLine(15, "    text=\"Presióname\","),
            CodeLine(16, "    command=al_hacer_clic,"),
            CodeLine(17, "    bg=\"#007bff\","),
            CodeLine(18, "    fg=\"white\","),
            CodeLine(19, "    font=(\"Arial\", 12),"),
            CodeLine(20, "    padx=20,"),
            CodeLine(21, "    pady=10"),
            CodeLine(22, ")"),
            CodeLine(23, "boton.pack(pady=20)"),
            CodeLine(24, ""),
            CodeLine(25, "# Etiqueta para mostrar resultado"),
            CodeLine(26, "etiqueta = tk.Label(ventana, text=\"\", font=(\"Arial\", 10))"),
            CodeLine(27, "etiqueta.pack()"),
            CodeLine(28, ""),
            CodeLine(29, "ventana.mainloop()")
        )

        val sampleSnippetDetail = SnippetDetail(code = sampleCode)

        val emptySnippetDetail = SnippetDetail(
            breadcrumb = listOf("Mis Recetas", "Nuevo Snippet"),
            title = "",
            tags = emptyList(),
            createdDate = "Hoy",
            usageCount = 0,
            fileName = "archivo.ext",
            description = "",
            dependencies = "",
            evidenceText = "",
            code = emptyList()
        )
    }
}
