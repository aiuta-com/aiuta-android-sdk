package com.aiuta.fashionsdk.configuration.mode.shoes.picker.model

import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.AiutaShoesModeImagePicker
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.strings.AiutaShoesModeImagePickerPredefinedModelsStrings

/**
 * Shoes-mode overrides for the predefined-models selection in the image picker.
 *
 * Required components:
 * - [strings]: Text strings for the predefined-models selection
 */
public class AiutaShoesModeImagePickerPredefinedModels(
    public val strings: AiutaShoesModeImagePickerPredefinedModelsStrings,
) {
    /**
     * Builder class for creating [AiutaShoesModeImagePickerPredefinedModels] instances.
     *
     * This builder ensures that all required components are provided before
     * creating the predefined-models instance.
     */
    @AiutaDsl
    public class Builder {
        public var strings: AiutaShoesModeImagePickerPredefinedModelsStrings? = null

        public fun build(): AiutaShoesModeImagePickerPredefinedModels {
            val parentClass = "AiutaShoesModeImagePickerPredefinedModels"

            return AiutaShoesModeImagePickerPredefinedModels(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the shoes-mode predefined models.
 *
 * Example usage:
 * ```
 * imagePicker {
 *     predefinedModels {
 *         strings = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaShoesModeImagePicker.Builder.predefinedModels(
    block: AiutaShoesModeImagePickerPredefinedModels.Builder.() -> Unit,
): AiutaShoesModeImagePicker.Builder = apply {
    predefinedModels = AiutaShoesModeImagePickerPredefinedModels.Builder().apply(block).build()
}
