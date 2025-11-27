package com.aiuta.fashionsdk.configuration.features.tryon.strings

/**
 * Interface for try-on feature text strings.
 *
 * This interface defines the text strings used in the try-on feature,
 * allowing for customization of user-facing text in the try-on UI.
 *
 * @property tryOnPageTitle Title text displayed at the top of the try-on page
 * @property tryOn Text for the try-on action button
 * @property outfitTitle Title for the outfit section listing items included in the outfit
 */
public interface AiutaTryOnFeatureStrings {
    public val tryOnPageTitle: String
    public val tryOn: String

    public val outfitTitle: String

    /**
     * Default implementation of [AiutaTryOnFeatureStrings].
     *
     * Provides standard English text strings for the try-on feature.
     */
    public class Default : AiutaTryOnFeatureStrings {
        override val tryOnPageTitle: String = "Virtual Try-on"
        override val tryOn: String = "Try on"
        override val outfitTitle: String = "Items from the outfit"
    }
}
