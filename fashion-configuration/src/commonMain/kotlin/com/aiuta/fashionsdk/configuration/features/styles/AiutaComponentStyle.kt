package com.aiuta.fashionsdk.configuration.features.styles

/**
 * Defines the available visual styles for Aiuta UI components.
 *
 * Each style represents a different appearance configuration that can be applied to UI elements,
 * such as buttons, to achieve a consistent look and feel across the application.
 */
public enum class AiutaComponentStyle {
    /**
     * Primary button style with solid background and high contrast.
     * Typically used for main actions and calls to action.
     */
    BRAND,

    /**
     * High-contrast style for components that require strong visual emphasis.
     */
    CONTRAST,

    /**
     * Inverted high-contrast style, suitable for use on dark backgrounds.
     */
    CONTRAST_INVERTED,

    /**
     * Blurred button style with translucent background.
     * Provides a frosted-glass effect for subtle emphasis.
     */
    BLURRED,

    /**
     * Blurred button style with an additional outline border.
     * Combines the frosted-glass effect with a visible border for extra distinction.
     */
    BLURRED_WITH_OUTLINE,
}
