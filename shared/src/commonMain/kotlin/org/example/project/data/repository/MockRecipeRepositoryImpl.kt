package org.example.project.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import org.example.project.domain.model.SnippetCardData
import org.example.project.domain.repository.RecipeRepository
import org.example.project.presentation.theme.DevSpaceColors

class MockRecipeRepositoryImpl : RecipeRepository {
    private val sampleCards = listOf(
        SnippetCardData(
            title = "Grupo A",
            languageTag = "React",
            secondaryTag = "UI",
            iconAccent = DevSpaceColors.primary,
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
            title = "Grupo B",
            languageTag = "Python",
            secondaryTag = "Data",
            iconAccent = DevSpaceColors.secondary,
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
            title = "Grupo C",
            languageTag = "SQL",
            secondaryTag = "Analytics",
            iconAccent = DevSpaceColors.tertiary,
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
        )
    )

    override suspend fun getPersonalRecipes(): List<SnippetCardData> {
        return sampleCards
    }
}
