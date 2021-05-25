package dev.prochnow.bdayreminder.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import dev.prochnow.bdayreminder.CategoryModel

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

@Stable
data class AppColors(
    val selectedCategoryColor: Color,
    val materialColors: Colors,
    val categoryPalette: Map<CategoryModel, Color>
)

private val catergoryLightColors = AppColors(
    LightColorPalette.secondary,
    LightColorPalette,
    mapOf(
        CategoryModel.NONE to CategoryLightColors.none,
        CategoryModel.WORK to CategoryLightColors.work,
        CategoryModel.FRIENDS to CategoryLightColors.friends,
        CategoryModel.FAMILY to CategoryLightColors.family
    )
)

private val catergoryDarkColors = AppColors(
    DarkColorPalette.secondary,
    DarkColorPalette,
    mapOf(
        CategoryModel.NONE to CategoryDarkColors.none,
        CategoryModel.WORK to CategoryDarkColors.work,
        CategoryModel.FRIENDS to CategoryDarkColors.friends,
        CategoryModel.FAMILY to CategoryDarkColors.family
    )
)

@Composable
fun appColors(
    colorPalette: CategoryModel = CategoryModel.NONE,
    darkTheme: Boolean = false
): AppColors =
    when (colorPalette) {
        CategoryModel.NONE -> if (darkTheme) catergoryDarkColors else catergoryLightColors
        CategoryModel.FAMILY -> if (darkTheme) catergoryDarkColors else catergoryLightColors
        CategoryModel.FRIENDS -> if (darkTheme) catergoryDarkColors else catergoryLightColors
        CategoryModel.WORK -> if (darkTheme) catergoryDarkColors else catergoryLightColors
    }

object CategoryTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

private val LocalAppColors = staticCompositionLocalOf {
    catergoryLightColors // For instance, pink dark colors
}

@Composable
fun CategoryTheme(
    colorPalette: CategoryModel = CategoryModel.NONE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = appColors(colorPalette = colorPalette, darkTheme = darkTheme)
    CompositionLocalProvider(
        LocalAppColors provides colors,
    ) {
        MaterialTheme(
            colors = colors.materialColors.copy(
                primary = animateColorAsState(
                    targetValue = colors.categoryPalette[colorPalette]
                        ?: colors.materialColors.primary,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium
                    )
                ).value
            ),
            content = content,
        )
    }
}



