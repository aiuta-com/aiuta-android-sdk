package com.aiuta.fashionsdk.configuration.features.sizefit.strings

/**
 * Interface for size fit feature text strings.
 *
 * This interface defines the text strings used in the size fit feature,
 * allowing for customization of user-facing text in the size fit UI.
 *
 * @property questionaryTitle Title for the questionnaire page
 * @property genderMale Male gender option
 * @property genderFemale Female gender option
 * @property agePlaceholder Placeholder for year of birth input
 * @property heightPlaceholder Placeholder for height input
 * @property heightSuffix Suffix for height value (e.g., "CM")
 * @property weightPlaceholder Placeholder for weight input
 * @property weightSuffix Suffix for weight value (e.g., "KG")
 * @property silhouetteTitle Title for the silhouette section
 * @property bellyAndHipsShapePlaceholder Placeholder for belly and hips shape selector
 * @property braSizePlaceholder Placeholder for bra size selector
 * @property findYourSizeButton Text for the find your size button
 * @property privacyPolicyHtml HTML-formatted text with privacy policy link
 * @property errorTitle Title for the error state
 * @property errorMessage Message explaining why size cannot be recommended
 * @property parametersTitle Title for the parameters section
 * @property changeButton Text for the change button
 * @property gotItButton Text for the got it button
 * @property recommendedSizeTitle Title for the recommended size result
 * @property doneButton Text for the done button used in sub-features
 */
public interface AiutaSizeFitFeatureStrings {

    // Questionary screen
    public val questionaryTitle: String

    // Base
    public val genderMale: String
    public val genderFemale: String
    public val agePlaceholder: String

    // Height
    public val heightPlaceholder: String
    public val heightSuffix: String

    // Weight
    public val weightPlaceholder: String
    public val weightSuffix: String

    // Silhouette
    public val silhouetteTitle: String
    public val bellyAndHipsShapePlaceholder: String
    public val braSizePlaceholder: String
    public val findYourSizeButton: String
    public val privacyPolicyHtml: String

    // Result screen
    public val parametersTitle: String
    public val changeButton: String
    public val recommendedSizeTitle: String
    public val doneButton: String

    // General
    // Error
    public val errorMessage: String

    // Buttons
    public val nextButton: String
    public val gotItButton: String

    /**
     * Default implementation of [AiutaSizeFitFeatureStrings].
     *
     * Provides standard English text strings for the size fit feature,
     * including a link to the privacy policy.
     *
     * @param privacyPolicyUrl URL to the privacy policy document
     */
    public class Default(
        privacyPolicyUrl: String,
    ) : AiutaSizeFitFeatureStrings {
        // Questionary screen
        override val questionaryTitle: String = "Find your size"

        // Base
        override val genderMale: String = "Male"
        override val genderFemale: String = "Female"
        override val agePlaceholder: String = "Age"
        override val heightPlaceholder: String = "Height"
        override val heightSuffix: String = "CM"
        override val weightPlaceholder: String = "Weight"
        override val weightSuffix: String = "KG"

        // Silhouette
        override val silhouetteTitle: String = "Your silhouette"
        override val bellyAndHipsShapePlaceholder: String = "Belly and hips shape"
        override val braSizePlaceholder: String = "Bra size"
        override val findYourSizeButton: String = "Find your size"
        override val privacyPolicyHtml: String =
            "Your data will be processed under our <b><a href=\"$privacyPolicyUrl\">Privacy Policy</a></b>"

        // Result screen
        override val parametersTitle: String = "Parameters"
        override val changeButton: String = "Change"
        override val recommendedSizeTitle: String = "Recommended size"
        override val doneButton: String = "Done"

        // General
        // Error
        override val errorMessage: String =
            "Sorry!\nThis item is designed for a different body type or gender. We can't recommend a size"
        override val nextButton: String = "Next"

        // Buttons
        override val gotItButton: String = "Got it"
    }
}
