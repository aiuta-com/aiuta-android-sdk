package com.aiuta.fashionsdk.configuration.features.tryon.loading.styles

import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.configuration.features.styles.AiutaComponentStyle

/**
 * Interface for loading page visual styles.
 *
 * This interface defines the visual styles used in the loading interface,
 * allowing for customization of the appearance of loading UI elements.
 *
 * @property loadingStatusBackgroundGradient Optional gradient colors for the loading status background
 * @property loadingStatusStyle Style for the loading status text and container
 */
public interface AiutaTryOnLoadingPageFeatureStyles {
    public val loadingStatusBackgroundGradient: List<Color>?
    public val loadingStatusStyle: AiutaComponentStyle

    /**
     * Default implementation of [AiutaTryOnLoadingPageFeatureStyles].
     *
     * Provides standard visual styles for the loading interface,
     * using a blue gradient background and blurred button style.
     */
    public class Default : AiutaTryOnLoadingPageFeatureStyles {
        override val loadingStatusBackgroundGradient: List<Color> = listOf(
            Color(0xFF4000FF),
            Color.Transparent,
        )
        override val loadingStatusStyle: AiutaComponentStyle = AiutaComponentStyle.BLURRED
    }
}
