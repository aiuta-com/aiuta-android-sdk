package com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.strings

/**
 * Interface defining text strings used in the shoes-mode predefined-models selection.
 */
public interface AiutaShoesModeImagePickerPredefinedModelsStrings {
    /**
     * Title of example page for shoes
     */
    public val predefinedModelShoesPageTitle: String

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
        override val predefinedModelShoesPageTitle: String = "Select example"
        override val predefinedModelShoesCategories: Map<String, String> = buildMap {
            put(key = "full-height", value = "Full body view")
            put(key = "bird-view", value = "Top-view shoe")
            put(key = "side-view", value = "Side-view shoe")
        }
    }
}
