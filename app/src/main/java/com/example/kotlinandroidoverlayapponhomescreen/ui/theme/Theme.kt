package com.example.kotlinandroidoverlayapponhomescreen.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Futuristic blue color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D9FF),        // Neon blue
    onPrimary = Color(0xFF0A192F),     // Dark blue
    secondary = Color(0xFF4DD0E1),    // Cyan
    onSecondary = Color(0xFF0A192F),
    tertiary = Color(0xFF2196F3),      // Bright blue
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF0A192F),     // Dark blue background
    onBackground = Color(0xFFE0F7FA),   // Light cyan text
    surface = Color(0xFF1A2332),        // Slightly lighter dark blue
    onSurface = Color(0xFFE0F7FA),
    surfaceVariant = Color(0xFF2A3441),
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = Color(0xFFF44336),
    onError = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),        // Deep blue
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF00BCD4),     // Cyan
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF2196F3),      // Bright blue
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF5F9FF),     // Very light blue
    onBackground = Color(0xFF0A192F),   // Dark blue text
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0A192F),
    surfaceVariant = Color(0xFFE3F2FD),
    onSurfaceVariant = Color(0xFF546E7A),
    error = Color(0xFFF44336),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun MLBBTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
