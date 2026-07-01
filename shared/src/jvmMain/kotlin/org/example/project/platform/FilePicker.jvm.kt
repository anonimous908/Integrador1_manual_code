package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (ByteArray) -> Unit): () -> Unit {
    return remember {
        {
            val chooser = JFileChooser().apply {
                dialogTitle = "Seleccionar imagen"
                fileFilter = FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif", "bmp", "webp")
            }
            val result = chooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val file = chooser.selectedFile
                try {
                    val bytes = file.readBytes()
                    onImagePicked(bytes)
                } catch (_: Exception) { }
            }
        }
    }
}
