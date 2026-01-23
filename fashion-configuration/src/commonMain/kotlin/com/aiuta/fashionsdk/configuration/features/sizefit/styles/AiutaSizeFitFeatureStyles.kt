package com.aiuta.fashionsdk.configuration.features.sizefit.styles

import androidx.compose.ui.graphics.Color

/**
 * Interface for size fit feature styles.
 *
 * This interface defines the visual styles used in the size fit feature,
 * allowing for customization of the UI appearance.
 *
 * @property sizeFitButtonGradient Optional list of colors for the size fit button gradient
 */
public interface AiutaSizeFitFeatureStyles {
    public val sizeFitButtonGradient: List<Color>

    public val suffixColor: Color

    /**
     * Default implementation of [AiutaSizeFitFeatureStyles].
     *
     * Provides standard styling for the size fit feature,
     * with no custom gradient for the size fit button.
     */
    public class Default : AiutaSizeFitFeatureStyles {
        override val sizeFitButtonGradient: List<Color> = listOf(
            Color(0xFF4372FF),
            Color(0xFF726FFF),
        )
        override val suffixColor: Color = Color(0xFF4000FF)
    }
}
