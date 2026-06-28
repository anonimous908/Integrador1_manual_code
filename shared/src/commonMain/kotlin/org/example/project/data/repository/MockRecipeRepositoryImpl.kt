package org.example.project.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import org.example.project.domain.model.SnippetCardData
import org.example.project.domain.repository.RecipeRepository
import org.example.project.presentation.theme.CodeNestColors

class MockRecipeRepositoryImpl : RecipeRepository {
    private val sampleCards = listOf(
        SnippetCardData(
            title = "Grupo A1",
            languageTag = "React",
            secondaryTag = "UI",
            iconAccent = CodeNestColors.primary,
            activeLineIndex = 3,
            footerLabel = "Evidence",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "import { motion } from 'framer-motion';",
                "",
                "export const Tabs = ({ items }) => {",
                "  return (",
                "    <div className=\"flex space-x-1 rounded-xl bg-blue-900/20 p-1\">",
                "      {items.map((item) => (",
                "        <motion.button layoutId=\"activeTab\">",
                "          {item.label}",
                "        </motion.button>",
                "      ))}",
                "    </div>",
                "  );",
                "};"
            )
        ),
        SnippetCardData(
            title = "Grupo A2",
            languageTag = "Python",
            secondaryTag = "Data",
            iconAccent = CodeNestColors.secondary,
            activeLineIndex = 5,
            footerLabel = "Output Log",
            footerIcon = Icons.Filled.Visibility,
            showTerminalOutput = true,
            terminalLines = listOf(
                "> python clean.py",
                "[INFO] Loading dataset...",
                "[INFO] Initial shape: (10500, 42)",
                "[INFO] Dropping sparse rows...",
                "[INFO] Final shape: (9820, 42)",
                "[SUCCESS] Data cleaned in 0.4s"
            ),
            codeLines = listOf(
                "import pandas as pd",
                "import numpy as np",
                "",
                "def clean_dataset(df):",
                "    \"\"\"Removes nulls and normalizes text columns\"\"\"",
                "    df = df.dropna(thresh=int(0.8 * len(df.columns)))",
                "",
                "    for col in df.select_dtypes(include=['object']):",
                "        df[col] = df[col].str.lower().str.strip()",
                "",
                "    return df",
                "",
                "cleaned_data = clean_dataset(raw_data)"
            )
        ),
        SnippetCardData(
            title = "Grupo A3",
            languageTag = "SQL",
            secondaryTag = "Analytics",
            iconAccent = CodeNestColors.tertiary,
            activeLineIndex = 3,
            footerLabel = "Visualization",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "WITH cohort_items AS (",
                "  SELECT",
                "    user_id,",
                "    DATE_TRUNC('month', MIN(created_at)) AS cohort_month",
                "  FROM subscriptions",
                "  GROUP BY 1",
                ")",
                "SELECT",
                "  c.cohort_month,",
                "  COUNT(DISTINCT s.user_id) AS active_users",
                "FROM cohort_items c",
                "LEFT JOIN subscriptions s ON c.user_id = s.user_id"
            )
        ),
        SnippetCardData(
            title = "Grupo A4",
            languageTag = "Kotlin",
            secondaryTag = "Android",
            iconAccent = CodeNestColors.primary,
            activeLineIndex = 2,
            footerLabel = "Component Preview",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "@Composable",
                "fun Greeting(name: String) {",
                "    Text(",
                "        text = \"Hello \$name!\",",
                "        modifier = Modifier.padding(24.dp)",
                "    )",
                "}"
            )
        ),
        SnippetCardData(
            title = "Grupo A5",
            languageTag = "HTML",
            secondaryTag = "Web",
            iconAccent = CodeNestColors.secondary,
            activeLineIndex = 4,
            footerLabel = "Browser",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "<!DOCTYPE html>",
                "<html>",
                "<head>",
                "  <title>CodeNest App</title>",
                "  <link rel=\"stylesheet\" href=\"styles.css\">",
                "</head>",
                "<body>",
                "  <div id=\"root\"></div>",
                "</body>",
                "</html>"
            )
        ),
        SnippetCardData(
            title = "Grupo A6",
            languageTag = "Bash",
            secondaryTag = "Scripts",
            iconAccent = CodeNestColors.tertiary,
            activeLineIndex = 1,
            footerLabel = "Terminal",
            footerIcon = Icons.Filled.Visibility,
            showTerminalOutput = true,
            terminalLines = listOf(
                "$ ./deploy.sh",
                "Building project...",
                "Deploying to server...",
                "Done!"
            ),
            codeLines = listOf(
                "#!/bin/bash",
                "echo \"Starting deployment...\"",
                "npm run build",
                "scp -r dist/* user@server:/var/www/html",
                "echo \"Deployment complete!\""
            )
        ),
        SnippetCardData(
            title = "Grupo A7",
            languageTag = "Swift",
            secondaryTag = "iOS",
            iconAccent = CodeNestColors.primary,
            activeLineIndex = 3,
            footerLabel = "Simulator",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "import SwiftUI",
                "",
                "struct ContentView: View {",
                "    var body: some View {",
                "        Text(\"Hello, SwiftUI!\")",
                "            .padding()",
                "    }",
                "}"
            )
        ),
        SnippetCardData(
            title = "Grupo A8",
            languageTag = "Go",
            secondaryTag = "Backend",
            iconAccent = CodeNestColors.secondary,
            activeLineIndex = 4,
            footerLabel = "Log",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "package main",
                "",
                "import \"fmt\"",
                "",
                "func main() {",
                "    fmt.Println(\"Server listening on :8080\")",
                "}"
            )
        ),
        SnippetCardData(
            title = "Grupo A9",
            languageTag = "Rust",
            secondaryTag = "Systems",
            iconAccent = CodeNestColors.tertiary,
            activeLineIndex = 2,
            footerLabel = "Console",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "fn main() {",
                "    let msg = \"Safe and fast!\";",
                "    println!(\"{}\", msg);",
                "}"
            )
        ),
        SnippetCardData(
            title = "Grupo A10",
            languageTag = "Docker",
            secondaryTag = "DevOps",
            iconAccent = CodeNestColors.primary,
            activeLineIndex = 3,
            footerLabel = "Build",
            footerIcon = Icons.Filled.Visibility,
            codeLines = listOf(
                "FROM node:18-alpine",
                "WORKDIR /app",
                "COPY package*.json ./",
                "RUN npm install",
                "COPY . .",
                "EXPOSE 3000",
                "CMD [\"npm\", \"start\"]"
            )
        )
    )

    override suspend fun getPersonalRecipes(): List<SnippetCardData> {
        return sampleCards
    }
}
