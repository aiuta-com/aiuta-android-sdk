package com.aiuta.fashionsdk.configuration.features.picker.model.strings

/**
 * Interface defining text strings used in the predefined models feature.
 *
 * This interface provides strings for titles, messages, and category names
 * displayed in the predefined models selection interface.
 */
public interface AiutaImagePickerPredefinedModelFeatureStrings {
    /**
     * Main button to navigate to predefined models selection page.
     */
    public val predefinedModelPageButton: String

    /**
     * Text displayed as a separator between different model selection options.
     */
    public val predefinedModelOr: String

    /**
     * Error message displayed when no models are available for selection.
     */
    public val predefinedModelErrorEmptyModelsList: String

    /**
     * Map of category IDs to their display names.
     * Used to show localized category names in the model selection interface.
     */
    public val predefinedModelCategories: Map<String, String>

    /**
     * Default implementation of [AiutaImagePickerPredefinedModelFeatureStrings].
     *
     * Provides standard English text for the predefined models interface.
     */
    public class Default : AiutaImagePickerPredefinedModelFeatureStrings {
        override val predefinedModelPageButton: String = "Select your model"
        override val predefinedModelOr: String = "Or"
        override val predefinedModelErrorEmptyModelsList: String = "The models list is empty"
        override val predefinedModelCategories: Map<String, String> = buildMap {
            put(key = "female", value = "Women")
            put(key = "male", value = "Men")
        }
    }
}
