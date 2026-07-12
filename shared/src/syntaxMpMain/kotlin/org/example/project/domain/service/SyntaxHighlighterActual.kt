package org.example.project.domain.service

import org.example.project.data.formatter.SyntaxMPHighlighter

actual fun getSyntaxHighlighter(): SyntaxHighlighter = SyntaxMPHighlighter()
