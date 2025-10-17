package com.nevoit.cresto.ui.theme.glasense

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
    onBackground = Color.White,
    onSurface = Color.White,
    primary = Blue500,
    background = Color.Black,
    surface = Color(0xFF1B1C1D),
)

private val LightColorScheme = lightColorScheme(
    onBackground = Color.Black,
    onSurface = Color.Black,
    primary = Blue500,
    background = Color.White,
    surface = Color(0xFFF3F4F6)
)

@Composable
fun GlasenseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
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