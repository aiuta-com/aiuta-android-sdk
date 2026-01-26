package com.aiuta.fashionsdk.compose.uikit.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.compose.uikit.button.internal.DefaultFashionButtonStyle
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonColors
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonColors.transparentColors
import com.aiuta.fashionsdk.compose.uikit.button.internal.FashionButtonStyle
import com.aiuta.fashionsdk.compose.uikit.button.internal.GradientButtonStyle
import com.aiuta.fashionsdk.compose.uikit.button.internal.SecondaryFashionButtonStyle
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.color.AiutaColorThemeScheme

/**
 * A collection of predefined button styles for the Fashion SDK.
 * These styles provide consistent visual appearance across the application.
 */
@Immutable
public object FashionButtonStyles {
    /**
     * Creates a primary button style using the provided theme.
     *
     * @param theme The theme to use for the button colors
     * @return A primary button style with theme-based colors
     */
    @Composable
    public fun primaryStyle(theme: AiutaTheme): FashionButtonStyle = DefaultFashionButtonStyle(
        colors = FashionButtonColors.primaryColors(theme = theme),
    )

    /**
     * Creates a primary button style with custom colors.
     *
     * @param backgroundColor The background color of the button
     * @param contentColor The color of the button's content (text and icon)
     * @return A primary button style with custom colors
     */
    @Composable
    public fun primaryStyle(
        backgroundColor: Color,
        contentColor: Color,
    ): FashionButtonStyle = DefaultFashionButtonStyle(
        colors = FashionButtonColors.primaryColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
    )

    /**
     * Creates a gradient button style with custom colors and gradient background.
     *
     * @param contentColor The color of the button's content (text and icon)
     * @param gradientBackground The gradient brush for the button background
     * @return A gradient button style with custom colors and background
     */
    @Composable
    public fun gradientColors(
        contentColor: Color,
        gradientBackground: Brush,
    ): FashionButtonStyle = GradientButtonStyle(
        colors = transparentColors(contentColor = contentColor),
        gradientBackground = gradientBackground,
    )

    /**
     * Creates a secondary button style using the provided theme.
     *
     * @param theme The theme to use for the button colors
     * @return A secondary button style with theme-based colors
     */
    @Composable
    public fun secondaryStyle(theme: AiutaTheme): FashionButtonStyle = SecondaryFashionButtonStyle(
        colors = FashionButtonColors.secondaryColors(theme = theme),
    )

    /**
     * Creates a secondary button style with custom colors.
     *
     * @param backgroundColor The background color of the button
     * @param contentColor The color of the button's content (text and icon)
     * @param borderColor The color of the button's border
     * @return A secondary button style with custom colors
     */
    @Composable
    public fun secondaryStyle(
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color,
    ): FashionButtonStyle = SecondaryFashionButtonStyle(
        colors = FashionButtonColors.secondaryColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            borderColor = borderColor,
        ),
    )

    /**
     * Creates a contrast button style using the provided theme.
     * This style uses dark background with light content for high contrast.
     *
     * @param theme The theme to use for the button colors
     * @return A contrast button style with theme-based colors
     */
    @Composable
    public fun contrastStyle(
        theme: AiutaTheme,
    ): FashionButtonStyle = SecondaryFashionButtonStyle(
        colors = FashionButtonColors.secondaryColors(
            backgroundColor = theme.color.onDark,
            contentColor = theme.color.onLight,
            borderColor = Color.Transparent,
        ),
    )

    /**
     * Creates an inverted contrast button style using the provided theme.
     * This style uses light background with dark content for high contrast.
     *
     * @param theme The theme to use for the button colors
     * @return An inverted contrast button style with theme-based colors
     */
    @Composable
    public fun contrastInvertedStyle(
        theme: AiutaTheme,
    ): FashionButtonStyle = SecondaryFashionButtonStyle(
        colors = FashionButtonColors.secondaryColors(
            backgroundColor = theme.color.onLight,
            contentColor = theme.color.onDark,
            borderColor = Color.Transparent,
        ),
    )

    /**
     * Creates an adaptive contrast button style that automatically adjusts based on the theme's color scheme.
     * Uses contrast style for light theme and inverted contrast style for dark theme.
     *
     * @param theme The theme to use for the button colors
     * @return An adaptive contrast button style that changes based on the theme
     */
    @Composable
    public fun adaptiveContrastStyle(
        theme: AiutaTheme,
    ): FashionButtonStyle = when (theme.color.scheme) {
        AiutaColorThemeScheme.LIGHT -> contrastStyle(theme)
        AiutaColorThemeScheme.DARK -> contrastInvertedStyle(theme)
    }
}
