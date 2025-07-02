package com.aiuta.fashionsdk.configuration.ui.theme.indicator.colors

import androidx.compose.ui.graphics.Color

/**
 * Defines the color scheme for the Aiuta activity indicator component.
 */
public interface AiutaActivityIndicatorThemeColors {

    /**
     * The color used for the overlay behind the activity indicator.
     */
    public val overlay: Color

    /**
     * Default implementation of [AiutaActivityIndicatorThemeColors] with a fully transparent overlay.
     */
    public class Default : AiutaActivityIndicatorThemeColors {
        override val overlay: Color = Color(0x00000000)
    }
}
