package com.aiuta.fashionsdk.configuration.features.sizefit.belly.strings

/**
 * Interface for belly shape feature text strings.
 *
 * This interface defines the text strings used in the belly shape selection interface,
 * allowing for customization of user-facing text in the belly shape UI.
 *
 * @property bellyShapeTitle Title for the belly shape section
 * @property hipsShapeTitle Title for the hips shape section
 */
public interface AiutaSizeFitBellyFeatureStrings {
    public val bellyShapeTitle: String
    public val hipsShapeTitle: String

    /**
     * Default implementation of [AiutaSizeFitBellyFeatureStrings].
     *
     * Provides standard English text strings for the belly shape selection interface.
     */
    public class Default : AiutaSizeFitBellyFeatureStrings {
        override val bellyShapeTitle: String = "Belly shape"
        override val hipsShapeTitle: String = "Hips shape"
    }
}
