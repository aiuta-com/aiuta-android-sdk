package com.aiuta.fashionsdk.configuration.features.tryon.loading.styles

import androidx.compose.ui.graphics.Color

/**
 * Interface for loading page visual styles.
 *
 * This interface defines the visual styles used in the loading interface,
 * allowing for customization of the appearance of loading UI elements.
 *
 * @property loadingStatusBackgroundGradient Optional gradient colors for the loading status background
 */
public interface AiutaTryOnLoadingPageFeatureStyles {
    public val loadingStatusBackgroundGradient: List<Color>?

    /**
     * Default implementation of [AiutaTryOnLoadingPageFeatureStyles].
     *
     * Provides standard visual styles for the loading interface,
     * using a gradient background.
     */
    public class Default : AiutaTryOnLoadingPageFeatureStyles {
        override val loadingStatusBackgroundGradient: List<Color> = listOf(
            Color.White.copy(alpha = 0.5f),
            Color(0x00B1B1B1),
            Color.White.copy(alpha = 0.5f),
        )
    }
}
