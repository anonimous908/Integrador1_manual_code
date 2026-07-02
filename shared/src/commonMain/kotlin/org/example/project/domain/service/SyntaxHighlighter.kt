package org.example.project.domain.service

import androidx.compose.ui.text.AnnotatedString

interface SyntaxHighlighter {
    fun highlight(code: String, language: String): AnnotatedString
}

expect fun getSyntaxHighlighter(): SyntaxHighlighter
