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

private val DarkColorScheme = darkColorScheme(
    primary = InstaPink,
    onPrimary = Color.White,
    primaryContainer = InstaPurple,
    onPrimaryContainer = Color.White,
    secondary = InstaCoral,
    onSecondary = Color.Black,
    secondaryContainer = DarkCard,
    onSecondaryContainer = Color.White,
    tertiary = InstaGold,
    background = DarkBg,
    onBackground = Color(0xFFECEBF2),
    surface = DarkSurface,
    onSurface = Color(0xFFECEBF2),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFB5B3C8),
    outline = DarkCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = InstaPink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD8E4),
    onPrimaryContainer = Color(0xFF3B001D),
    secondary = InstaPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF2E004F),
    tertiary = InstaCoral,
    background = LightBg,
    onBackground = Color(0xFF191C1E),
    surface = LightSurface,
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFF1F3F9),
    onSurfaceVariant = Color(0xFF49454F),
    outline = LightCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our vibrant Instagram aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
