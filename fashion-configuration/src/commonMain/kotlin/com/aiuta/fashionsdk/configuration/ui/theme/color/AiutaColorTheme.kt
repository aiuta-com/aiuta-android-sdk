package com.aiuta.fashionsdk.configuration.ui.theme.color

import androidx.compose.ui.graphics.Color

/**
 * Color theme interface for the Aiuta SDK.
 *
 * This interface defines the color scheme and individual color values used throughout
 * the SDK's user interface. It provides a consistent color palette that can be
 * customized to match your app's branding.
 *
 *
 * @property scheme The color scheme mode (light or dark) for the theme
 * @property brand The brand color used for primary branding elements
 * @property primary The primary color used for main UI elements and text
 * @property secondary The secondary color used for supporting UI elements
 * @property onDark Color used for text and icons on dark backgrounds
 * @property onLight Color used for text and icons on light backgrounds
 * @property background The main background color for the app
 * @property screen The color used for screen backgrounds
 * @property neutral A neutral color used for subtle UI elements
 * @property border Color used for borders and dividers
 * @see AiutaColorThemeScheme
 */
public interface AiutaColorTheme {
    /**
     * The color scheme mode (light or dark) for the theme.
     * This determines the base color palette used throughout the UI.
     */
    public val scheme: AiutaColorThemeScheme

    /**
     * The brand color used for primary branding elements.
     * Typically used for logos, key UI elements, and brand-specific highlights.
     */
    public val brand: Color

    /**
     * The primary color used for main UI elements and text.
     * This is typically the most prominent color in the interface.
     */
    public val primary: Color

    /**
     * The secondary color used for supporting UI elements.
     * Used for less prominent elements and secondary text.
     */
    public val secondary: Color

    /**
     * Color used for text and icons on dark backgrounds.
     * Ensures proper contrast and readability on dark surfaces.
     */
    public val onDark: Color

    /**
     * Color used for text and icons on light backgrounds.
     * Ensures proper contrast and readability on light surfaces.
     */
    public val onLight: Color

    /**
     * The main background color for the app.
     * Used for the primary background of screens and containers.
     */
    public val background: Color

    /**
     * The color used for screen backgrounds.
     * Typically matches or complements the main background color.
     */
    public val screen: Color

    /**
     * A neutral color used for subtle UI elements.
     * Often used for dividers, inactive states, and subtle backgrounds.
     */
    public val neutral: Color

    /**
     * Color used for borders and dividers.
     * Provides visual separation between UI elements.
     */
    public val border: Color

    /**
     * Default implementation of [AiutaColorTheme].
     *
     * This class provides a standard light theme color palette that can be used
     * as a starting point for customization. The default colors are designed to
     * provide good contrast and readability while maintaining a clean, modern look.
     *
     * ```kotlin
     * theme {
     *     color = AiutaColorTheme.Default()
     * }
     * ```
     */
    public class Default : AiutaColorTheme {
        override val scheme: AiutaColorThemeScheme = AiutaColorThemeScheme.LIGHT
        override val brand: Color = Color(0xFF4000FF)
        override val primary: Color = Color(0xFF000000)
        override val secondary: Color = Color(0xFF9F9F9F)
        override val onDark: Color = Color(0xFFFFFFFF)
        override val onLight: Color = Color(0xFF000000)
        override val background: Color = Color(0xFFFFFFFF)
        override val screen: Color = Color(0xFFFFFFFF)
        override val neutral: Color = Color(0xFFF2F2F7)
        override val border: Color = Color(0xFFE5E5EA)
    }
}
