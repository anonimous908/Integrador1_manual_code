package org.example.project.data.formatter

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.presentation.theme.DevSpaceColors

class DraculaSyntaxHighlighter : SyntaxHighlighter {
    private val SQL_KEYWORDS = setOf(
        "WITH", "AS", "SELECT", "FROM", "GROUP", "BY", "LEFT", "JOIN", "ON",
        "DISTINCT", "WHERE", "ORDER"
    )
    private val GENERIC_KEYWORDS = setOf(
        "import", "from", "export", "const", "return", "def", "for", "in",
        "class", "if", "else", "while", "fun", "val", "var"
    )

    override fun highlight(line: String): AnnotatedString = buildAnnotatedString {
        if (line.isBlank()) {
            append(line)
            return@buildAnnotatedString
        }

        val trimmed = line.trimStart()
        if (trimmed.startsWith("#") || trimmed.startsWith("--") ||
            (trimmed.startsWith("\"\"\"") && trimmed.endsWith("\"\"\"") && trimmed.length > 3)
        ) {
            withStyle(SpanStyle(color = DevSpaceColors.codeComment)) { append(line) }
            return@buildAnnotatedString
        }

        val tokenRegex = Regex(
            "(\"[^\"]*\"|'[^']*')" + // strings
                "|\\b\\d+(\\.\\d+)?\\b" + // numbers
                "|\\b[A-Za-z_][A-Za-z0-9_]*\\b" // identifiers / keywords
        )

        var lastIndex = 0
        for (match in tokenRegex.findAll(line)) {
            if (match.range.first > lastIndex) {
                append(line.substring(lastIndex, match.range.first))
            }
            val token = match.value
            val style = when {
                token.startsWith("\"") || token.startsWith("'") -> SpanStyle(color = DevSpaceColors.codeString)
                token.firstOrNull()?.isDigit() == true -> SpanStyle(color = DevSpaceColors.codeNumber)
                token.uppercase() in SQL_KEYWORDS || token in GENERIC_KEYWORDS ->
                    SpanStyle(color = DevSpaceColors.codeKeyword)
                token.firstOrNull()?.isUpperCase() == true -> SpanStyle(color = DevSpaceColors.codeType)
                else -> SpanStyle(color = DevSpaceColors.onSurfaceVariant)
            }
            withStyle(style) { append(token) }
            lastIndex = match.range.last + 1
        }
        if (lastIndex < line.length) {
            append(line.substring(lastIndex))
        }
    }
}
