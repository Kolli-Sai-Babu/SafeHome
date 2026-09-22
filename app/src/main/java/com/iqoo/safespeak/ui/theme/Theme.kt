package com.iqoo.safespeak.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val iQOOBackground = Color(0xFF0D0E15)
val iQOOCardBg = Color(0xFF161824)
val iQOOCardBorder = Color(0xFF2A2D40)

val iQOONeonYellow = Color(0xFFFFD100)
val iQOOCyan = Color(0xFF00F0FF)
val iQOOEmergencyRed = Color(0xFFFF3B30)
val iQOOSuccessGreen = Color(0xFF34C759)

val TextPrimary = Color(0xFFF5F6FA)
val TextSecondary = Color(0xFFA0A6C0)

private val DarkColorScheme = darkColorScheme(
    primary = iQOONeonYellow,
    onPrimary = Color.Black,
    secondary = iQOOCyan,
    onSecondary = Color.Black,
    tertiary = iQOOSuccessGreen,
    background = iQOOBackground,
    surface = iQOOCardBg,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = iQOOEmergencyRed
)

@Composable
fun SafeSpeakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
