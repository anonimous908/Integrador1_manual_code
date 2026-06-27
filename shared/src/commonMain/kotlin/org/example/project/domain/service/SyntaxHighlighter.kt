package org.example.project.domain.service

import androidx.compose.ui.text.AnnotatedString

interface SyntaxHighlighter {
    fun highlight(line: String): AnnotatedString
}
