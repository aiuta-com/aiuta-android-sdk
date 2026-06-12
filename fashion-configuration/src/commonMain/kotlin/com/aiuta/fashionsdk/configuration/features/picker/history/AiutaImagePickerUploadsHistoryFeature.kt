package com.aiuta.fashionsdk.configuration.features.picker.history

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.dataprovider.AiutaImagePickerUploadsHistoryFeatureDataProvider
import com.aiuta.fashionsdk.configuration.features.picker.history.strings.AiutaImagePickerUploadsHistoryFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the uploads history feature in the image picker.
 *
 * This feature provides functionality to view and manage previously uploaded images,
 * including text strings, visual styles, and data management.
 *
 * Required components:
 * - [strings]: Text strings for the history interface
 * - [dataProvider]: Provider for history data operations
 */
public class AiutaImagePickerUploadsHistoryFeature(
    public val strings: AiutaImagePickerUploadsHistoryFeatureStrings,
    public val dataProvider: AiutaImagePickerUploadsHistoryFeatureDataProvider,
) : AiutaFeature {
    /**
     * Builder class for creating [AiutaImagePickerUploadsHistoryFeature] instances.
     *
     * This builder ensures that all required components are provided before
     * creating the feature instance.
     */
    public class Builder : AiutaFeature.Builder {
        public var strings: AiutaImagePickerUploadsHistoryFeatureStrings? = null
        public var dataProvider: AiutaImagePickerUploadsHistoryFeatureDataProvider? = null

        public override fun build(): AiutaImagePickerUploadsHistoryFeature {
            val parentClass = "AiutaImagePickerUploadsHistoryFeature"

            return AiutaImagePickerUploadsHistoryFeature(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
                dataProvider = dataProvider.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "dataProvider",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the uploads history feature.
 *
 * Example usage:
 * ```
 * imagePicker {
 *     uploadsHistory {
 *         strings = ...
 *         styles = ...
 *         dataProvider = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaImagePickerFeature.Builder.uploadsHistory(
    block: AiutaImagePickerUploadsHistoryFeature.Builder.() -> Unit,
): AiutaImagePickerFeature.Builder = apply {
    uploadsHistory = AiutaImagePickerUploadsHistoryFeature.Builder().apply(block).build()
}
