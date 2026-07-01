package org.example.project.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImagePicked: (ByteArray) -> Unit): () -> Unit
