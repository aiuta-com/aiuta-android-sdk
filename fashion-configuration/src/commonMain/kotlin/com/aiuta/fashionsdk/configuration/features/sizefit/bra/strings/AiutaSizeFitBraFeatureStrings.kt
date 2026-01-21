package com.aiuta.fashionsdk.configuration.features.sizefit.bra.strings

/**
 * Interface for bra size feature text strings.
 *
 * This interface defines the text strings used in the bra size selection interface,
 * allowing for customization of user-facing text in the bra size UI.
 *
 * @property braSizeTitle Title for the bra size section
 * @property chestCircumferenceTitle Title for the chest circumference section
 * @property braCupTitle Title for the bra cup section
 */
public interface AiutaSizeFitBraFeatureStrings {
    public val braSizeTitle: String
    public val chestCircumferenceTitle: String
    public val braCupTitle: String

    /**
     * Default implementation of [AiutaSizeFitBraFeatureStrings].
     *
     * Provides standard English text strings for the bra size selection interface.
     * Default chest circumference options range from 75 to 110 cm.
     */
    public class Default : AiutaSizeFitBraFeatureStrings {
        override val braSizeTitle: String = "Bra size"
        override val chestCircumferenceTitle: String = "Chest circumference"
        override val braCupTitle: String = "Bra cup"
    }
}
