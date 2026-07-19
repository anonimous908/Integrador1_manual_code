package org.example.project.domain.service

actual fun openLocalFile(fileName: String) {
    println("openLocalFile not implemented on Web: $fileName")
}
