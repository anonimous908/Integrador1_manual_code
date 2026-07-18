package org.example.project.domain.service

import java.io.File
import java.awt.Desktop

actual fun openLocalFile(fileName: String) {
    try {
        val paths = listOf(
            fileName,
            "CodeNest_Pantallas.pptx",
            "C:/Users/borge/.gemini/antigravity/worktrees/Integrador/integrate-manual-user-docs/CodeNest_Pantallas.pptx"
        )
        var opened = false
        for (path in paths) {
            val file = File(path)
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file)
                    opened = true
                    break
                } else {
                    Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "", file.absolutePath))
                    opened = true
                    break
                }
            }
        }
        if (!opened) {
            println("Could not open file: $fileName")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
