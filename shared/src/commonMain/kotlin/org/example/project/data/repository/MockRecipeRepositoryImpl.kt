package org.example.project.data.repository

import org.example.project.domain.model.Recipe
import org.example.project.domain.repository.RecipeRepository
import kotlin.uuid.Uuid

/**
 * Legacy mock repository returning sample recipes.
 * @deprecated Use [SettingsRecipeRepositoryImpl] in production or
 * [FakeRecipeRepositoryImpl] in tests instead.
 */
@Deprecated("Use SettingsRecipeRepositoryImpl for production or FakeRecipeRepositoryImpl for tests")
class MockRecipeRepositoryImpl : RecipeRepository {

    private val sampleRecipes = listOf(
        Recipe(
            id = Uuid.random().toString(),
            userId = "demo@codenest.app",
            title = "Grupo A",
            languageTag = "React",
            secondaryTag = "UI",
            code = listOf(
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
            ),
            description = "Animated tabs with Framer Motion",
            evidence = "Interactive component"
        ),
        Recipe(
            id = Uuid.random().toString(),
            userId = "demo@codenest.app",
            title = "Grupo B",
            languageTag = "Python",
            secondaryTag = "Data",
            code = listOf(
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
            ),
            description = "Data cleaning utility for pandas",
            evidence = "Output log available"
        ),
        Recipe(
            id = Uuid.random().toString(),
            userId = "demo@codenest.app",
            title = "Grupo C",
            languageTag = "SQL",
            secondaryTag = "Analytics",
            code = listOf(
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
            ),
            description = "Cohort retention analysis query",
            evidence = "Visualization attached"
        )
    )

    override suspend fun getPersonalRecipes(): List<Recipe> = sampleRecipes

    override suspend fun getById(id: String): Recipe? =
        sampleRecipes.find { it.id == id }

    override suspend fun create(recipe: Recipe): Recipe =
        throw UnsupportedOperationException("Mock repository does not support create. Use SettingsRecipeRepositoryImpl.")

    override suspend fun update(recipe: Recipe): Recipe =
        throw UnsupportedOperationException("Mock repository does not support update. Use SettingsRecipeRepositoryImpl.")

    override suspend fun delete(id: String): Boolean =
        throw UnsupportedOperationException("Mock repository does not support delete. Use SettingsRecipeRepositoryImpl.")
}
