package com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.strings

/**
 * Interface defining text strings used in the shoes-mode predefined-models selection.
 */
public interface AiutaShoesModeImagePickerPredefinedModelsStrings {
    /**
     * Map of category IDs to their display names.
     * Used to show localized category names in the model selection interface.
     */
    public val predefinedModelShoesCategories: Map<String, String>

    /**
     * Default implementation of [AiutaShoesModeImagePickerPredefinedModelsStrings].
     *
     * Provides standard English text for the shoes-mode predefined models interface.
     */
    public class Default : AiutaShoesModeImagePickerPredefinedModelsStrings {
        override val predefinedModelShoesCategories: Map<String, String> = buildMap {
            put(key = "male", value = "Men")
            put(key = "female", value = "Women")
        }
    }
}
