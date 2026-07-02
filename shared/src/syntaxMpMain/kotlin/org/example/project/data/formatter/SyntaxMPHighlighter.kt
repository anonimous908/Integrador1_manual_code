package org.example.project.data.formatter

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.gallatinapps.syntaxmp.compose.SyntaxStyle
import com.gallatinapps.syntaxmp.compose.SyntaxTheme
import com.gallatinapps.syntaxmp.compose.buildSyntaxAnnotatedString
import com.gallatinapps.syntaxmp.role.SyntaxRole
import com.gallatinapps.syntaxmp.tokenizer.SyntaxTokenizer
import org.example.project.domain.service.SyntaxHighlighter
import org.example.project.presentation.theme.DevSpaceColors

class SyntaxMPHighlighter : SyntaxHighlighter {
    private val engine = SyntaxTokenizer()

    private val draculaTheme = SyntaxTheme(
        roleStyles = mapOf(
            SyntaxRole.Keyword to SyntaxStyle(color = DevSpaceColors.codeKeyword, fontWeight = FontWeight.Bold),
            SyntaxRole.String to SyntaxStyle(color = DevSpaceColors.codeString),
            SyntaxRole.Type to SyntaxStyle(color = DevSpaceColors.codeType),
            SyntaxRole.Function to SyntaxStyle(color = DevSpaceColors.codeFunction),
            SyntaxRole.Comment to SyntaxStyle(color = DevSpaceColors.codeComment, fontStyle = FontStyle.Italic),
            SyntaxRole.Number to SyntaxStyle(color = DevSpaceColors.codeNumber),
            SyntaxRole.Variable to SyntaxStyle(color = DevSpaceColors.codeParam)
        )
    )

    override fun highlight(code: String, language: String): AnnotatedString {
        val normalizedLanguage = when (language.trim().lowercase()) {
            "react" -> "jsx"
            "javascript", "js" -> "javascript"
            "typescript", "ts" -> "typescript"
            else -> language
        }
        val spans = engine.tokenize(code = code, languageLabel = normalizedLanguage)
        return buildSyntaxAnnotatedString(code = code, spans = spans, theme = draculaTheme)
    }
}
