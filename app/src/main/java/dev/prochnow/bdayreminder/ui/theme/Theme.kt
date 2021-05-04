package dev.prochnow.bdayreminder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = purpleHeart,
    primaryVariant = purpleHeart,
    secondary = magnolia,
    background = whiteSmoke,
    surface = white,
    onPrimary = white,
    onSecondary = amethyst,
    onBackground = haiti,
    onSurface = haiti,
)

private val DarkColorPalette = lightColors(
    primary = purpleHeart,
    primaryVariant = purpleHeart,
    secondary = magnolia,
    background = whiteSmoke,
    surface = white,
    onPrimary = white,
    onSecondary = amethyst,
    onBackground = haiti,
    onSurface = haiti,
)

@Composable
fun BdayTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
