package org.example.project.presentation.theme

import org.example.project.presentation.theme.CodeNestColors
import org.example.project.presentation.theme.onPrimaryForBackground
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import androidx.compose.ui.graphics.Color

class CodeNestColorsTest {

    // ─── onPrimaryForBackground (existing pure function) ────────────────

    @Test
    fun `onPrimaryForBackground returns black for light background`() {
        // Color.WHITE has luminance 1.0 > 0.5 → should return black
        val result = onPrimaryForBackground(Color.White)
        assertEquals(Color.Black, result)
    }

    @Test
    fun `onPrimaryForBackground returns white for dark background`() {
        // Color.BLACK has luminance 0.0 < 0.5 → should return white
        val result = onPrimaryForBackground(Color.Black)
        assertEquals(Color.White, result)
    }

    @Test
    fun `onPrimaryForBackground returns black at exact threshold`() {
        // Luminance 0.5 is not > 0.5, so threshold color slightly above:
        // ~0.6 red gives luminance ~0.179 which is below threshold -> white
        // We need exact threshold test: a gray that's barely above 0.5
        val lightGray = Color(0xFFA0A0A0) // luminance ≈ 0.627 → > 0.5
        val result = onPrimaryForBackground(lightGray)
        assertEquals(Color.Black, result)
    }

    @Test
    fun `onPrimaryForBackground returns white just below threshold`() {
        val darkGray = Color(0xFF7A7A7A) // luminance ≈ 0.478 → < 0.5
        val result = onPrimaryForBackground(darkGray)
        assertEquals(Color.White, result)
    }

    // ─── updateSecondary ────────────────────────────────────────────────

    @Test
    fun `updateSecondary sets secondary and recalculates onSecondary`() {
        val testColor = Color(0xFFD7FFC5)
        CodeNestColors.updateSecondary(testColor)
        assertEquals(testColor, CodeNestColors.secondary)
        // onSecondary should be auto-calculated via luminance
        assertNotNull(CodeNestColors.onSecondary)
        // Luminance of 0xFFD7FFC5 is high → black text
        assertEquals(Color.Black, CodeNestColors.onSecondary)
    }

    @Test
    fun `updateSecondary with dark color results in white text`() {
        val darkColor = Color(0xFF003300)
        CodeNestColors.updateSecondary(darkColor)
        assertEquals(darkColor, CodeNestColors.secondary)
        assertEquals(Color.White, CodeNestColors.onSecondary)
    }

    // ─── updateTertiary ─────────────────────────────────────────────────

    @Test
    fun `updateTertiary sets tertiary and recalculates onTertiary`() {
        val testColor = Color(0xFFD7BAFF)
        CodeNestColors.updateTertiary(testColor)
        assertEquals(testColor, CodeNestColors.tertiary)
        assertNotNull(CodeNestColors.onTertiary)
    }

    @Test
    fun `updateTertiary with dark color results in white text`() {
        val darkColor = Color(0xFF2D004D)
        CodeNestColors.updateTertiary(darkColor)
        assertEquals(darkColor, CodeNestColors.tertiary)
        assertEquals(Color.White, CodeNestColors.onTertiary)
    }

    // ─── updateError ────────────────────────────────────────────────────

    @Test
    fun `updateError sets error and recalculates onError`() {
        val testColor = Color(0xFFFFB4AB)
        CodeNestColors.updateError(testColor)
        assertEquals(testColor, CodeNestColors.error)
        assertNotNull(CodeNestColors.onError)
    }

    @Test
    fun `updateError with dark error color`() {
        val darkError = Color(0xFF93000A)
        CodeNestColors.updateError(darkError)
        assertEquals(darkError, CodeNestColors.error)
        // dark red should give white text
        assertEquals(Color.White, CodeNestColors.onError)
    }

    // ─── updateSurface ──────────────────────────────────────────────────

    @Test
    fun `updateSurface sets all surface variants`() {
        CodeNestColors.updateSurface(Color(0xFF2A2A2A))
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surface)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceDim)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceBright)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceContainer)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceContainerLow)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceContainerLowest)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceContainerHigh)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceContainerHighest)
        assertEquals(Color(0xFF2A2A2A), CodeNestColors.surfaceVariant)
    }

    // ─── resetToDefaults ────────────────────────────────────────────────

    @Test
    fun `resetToDefaults restores secondary to default`() {
        CodeNestColors.updateTheme(true) // dark mode
        CodeNestColors.updateSecondary(Color(0xFFFF0000))
        CodeNestColors.updateTertiary(Color(0xFF00FF00))
        CodeNestColors.updateError(Color(0xFF0000FF))
        CodeNestColors.updateSurface(Color(0xFFAAAAAA))

        CodeNestColors.resetToDefaults()

        // After reset in dark mode, should go back to dark defaults
        assertEquals(Color(0xFFD7FFC5), CodeNestColors.secondary)
        assertEquals(Color(0xFFD7BAFF), CodeNestColors.tertiary)
        assertEquals(Color(0xFFFFB4AB), CodeNestColors.error)
    }

    @Test
    fun `resetToDefaults restores colors to light mode defaults when in light mode`() {
        CodeNestColors.updateTheme(false) // light mode
        CodeNestColors.updateSecondary(Color(0xFFFF0000))
        CodeNestColors.updateError(Color(0xFF0000FF))

        CodeNestColors.resetToDefaults()

        // After reset in light mode, check that colors are light-mode defaults
        // These will be verified against what updateTheme sets
        assertEquals(Color(0xFFD7FFC5), CodeNestColors.secondary)
        assertEquals(Color(0xFFFFB4AB), CodeNestColors.error)
    }
}
