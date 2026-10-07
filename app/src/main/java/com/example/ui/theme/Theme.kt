package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldBright,
    onPrimary = Color(0xFF281700),
    primaryContainer = Color(0xFF452B00),
    onPrimaryContainer = GoldLight,
    secondary = SaffronOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF431B00),
    onSecondaryContainer = Color(0xFFFFDBC8),
    tertiary = GoldGleam,
    onTertiary = Color.Black,
    background = DarkCosmicBg,
    onBackground = DarkTextPrimary,
    surface = DarkCosmicSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCosmicSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCosmicBorder,
    error = KumkumRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = TempleMaroon,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDADA),
    onPrimaryContainer = Color(0xFF410006),
    secondary = GoldAuspicious,
    onSecondary = Color.White,
    secondaryContainer = GoldLight,
    onSecondaryContainer = Color(0xFF2B1700),
    tertiary = SaffronOrange,
    onTertiary = Color.White,
    background = LightBg,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    error = KumkumRed,
    onError = Color.White
)

@Composable
fun TamilRasiPalanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
