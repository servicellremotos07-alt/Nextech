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

private val DarkColorScheme =
    darkColorScheme(
        primary = CyanPrimary,
        onPrimary = Color.Black,
        secondary = CyanDark,
        tertiary = PendingAmber,
        background = DarkBackground,
        surface = DarkSurface,
        onBackground = Color(0xFFF0F6FC),
        onSurface = Color(0xFFF0F6FC)
    )

private val LightColorScheme =
    lightColorScheme(
        primary = CyanDark,
        onPrimary = Color.White,
        secondary = CyanPrimary,
        tertiary = PendingAmber,
        background = LightSurface,
        surface = Color.White,
        onBackground = Color(0xFF1F2328),
        onSurface = Color(0xFF1F2328)
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled default dynamic color to maintain custom tech branding
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
