package org.example.project.data.formatter

import androidx.compose.ui.text.AnnotatedString
import org.example.project.domain.service.SyntaxHighlighter

class JsSyntaxHighlighter : SyntaxHighlighter {
    override fun highlight(code: String, language: String): AnnotatedString {
        return AnnotatedString(code)
    }
}
