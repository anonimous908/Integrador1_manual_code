package org.example.project

import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.example.project.presentation.components.calculateBrandPadding

class SharedCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun `calculateBrandPadding clamps to minimum 16dp for small sizes`() {
        // 100.dp * 0.03 = 3.dp, which is below the 16.dp minimum
        val result = calculateBrandPadding(100.dp)
        assertEquals(16.dp, result)
    }

    @Test
    fun `calculateBrandPadding returns proportional padding for large sizes`() {
        // 1000.dp * 0.03 = 30.dp, which is above the 16.dp minimum
        val result = calculateBrandPadding(1000.dp)
        assertEquals(30.dp, result)
    }

    @Test
    fun `calculateBrandPadding boundary at 534dp crosses minimum threshold`() {
        // 534.dp * 0.03 = 16.02.dp — just above the 16.dp minimum
        val result = calculateBrandPadding(534.dp)
        assertTrue(result > 16.dp, "Expected padding > 16dp for 534dp input, got $result")
        val expectedValue = 534f * 0.03f
        assertEquals(expectedValue, result.value, 0.01f)
    }
}