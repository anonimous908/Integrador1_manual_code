package org.example.project.domain.service

import org.example.project.data.formatter.JsSyntaxHighlighter

actual fun getSyntaxHighlighter(): SyntaxHighlighter = JsSyntaxHighlighter()
