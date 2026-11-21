package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val CiclomultLightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = OnGreenContainer,
    secondary = AmberAccent,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

private val CiclomultDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF00390A),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFFFFB74D),
    background = Color(0xFF121413),
    surface = Color(0xFF1A1C1A),
    onBackground = Color(0xFFE2E3E0),
    onSurface = Color(0xFFE2E3E0)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent green theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CiclomultDarkColorScheme else CiclomultLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
