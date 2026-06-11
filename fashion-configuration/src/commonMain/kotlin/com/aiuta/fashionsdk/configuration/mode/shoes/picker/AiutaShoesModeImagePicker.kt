package com.aiuta.fashionsdk.configuration.mode.shoes.picker

import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.images.AiutaShoesModeImagePickerImages
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.AiutaShoesModeImagePickerPredefinedModels
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.strings.AiutaShoesModeImagePickerStrings

/**
 * Shoes-mode overrides for the image picker surfaces.
 *
 * Required components:
 * - [predefinedModels]: Overrides for the predefined-models selection
 * - [images]: Images shown in the image picker
 * - [strings]: Text strings for the image picker
 */
public class AiutaShoesModeImagePicker(
    public val predefinedModels: AiutaShoesModeImagePickerPredefinedModels,
    public val images: AiutaShoesModeImagePickerImages,
    public val strings: AiutaShoesModeImagePickerStrings,
) {
    /**
     * Builder class for creating [AiutaShoesModeImagePicker] instances.
     *
     * This builder ensures that all required components are provided before
     * creating the image picker instance.
     */
    @AiutaDsl
    public class Builder {
        public var predefinedModels: AiutaShoesModeImagePickerPredefinedModels? = null
        public var images: AiutaShoesModeImagePickerImages? = null
        public var strings: AiutaShoesModeImagePickerStrings? = null

        public fun build(): AiutaShoesModeImagePicker {
            val parentClass = "AiutaShoesModeImagePicker"

            return AiutaShoesModeImagePicker(
                predefinedModels = predefinedModels.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "predefinedModels",
                ),
                images = images.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "images",
                ),
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the shoes-mode image picker.
 *
 * Example usage:
 * ```
 * shoes {
 *     imagePicker {
 *         predefinedModels { ... }
 *         images = ...
 *         strings = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaShoesMode.Builder.imagePicker(
    block: AiutaShoesModeImagePicker.Builder.() -> Unit,
): AiutaShoesMode.Builder = apply {
    imagePicker = AiutaShoesModeImagePicker.Builder().apply(block).build()
}
