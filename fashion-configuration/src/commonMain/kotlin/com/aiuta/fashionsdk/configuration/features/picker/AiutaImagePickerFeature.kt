package com.aiuta.fashionsdk.configuration.features.picker

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.picker.camera.AiutaImagePickerCameraFeature
import com.aiuta.fashionsdk.configuration.features.picker.gallery.AiutaImagePickerPhotoGalleryFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.picker.images.AiutaImagePickerFeatureImages
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.configuration.features.picker.protection.AiutaImagePickerProtectionDisclaimerFeature
import com.aiuta.fashionsdk.configuration.features.picker.strings.AiutaImagePickerFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Image picker feature configuration for the Aiuta SDK.
 *
 * This feature manages all aspects of image selection and management within
 * the SDK. It provides configuration for different image sources including
 * camera capture, photo gallery selection, predefined models, and upload history.
 *
 * Required components:
 * - [photoGallery]: Photo gallery selection functionality
 * - [images]: Image resources and placeholders
 * - [strings]: Text content and localization
 *
 * Optional components:
 * - [camera]: Camera capture functionality
 * - [predefinedModels]: Predefined model images
 * - [uploadsHistory]: Previously uploaded images history
 * - [protectionDisclaimer]: Privacy notice for uploaded photos
 *
 *
 * @property camera Optional camera capture feature configuration
 * @property photoGallery Required photo gallery selection feature configuration
 * @property predefinedModels Optional predefined model images feature configuration
 * @property protectionDisclaimer Optional protection disclaimer feature configuration
 * @property uploadsHistory Optional upload history feature configuration
 * @property images Required image resources and placeholders configuration
 * @property strings Required text content and localization configuration
 * @see AiutaFeature
 * @see AiutaImagePickerCameraFeature
 * @see AiutaImagePickerPhotoGalleryFeature
 */
@Immutable
public class AiutaImagePickerFeature(
    // Features
    public val camera: AiutaImagePickerCameraFeature?,
    public val photoGallery: AiutaImagePickerPhotoGalleryFeature,
    public val predefinedModels: AiutaImagePickerPredefinedModelFeature?,
    public val protectionDisclaimer: AiutaImagePickerProtectionDisclaimerFeature?,
    public val uploadsHistory: AiutaImagePickerUploadsHistoryFeature?,
    // General
    public val images: AiutaImagePickerFeatureImages,
    public val strings: AiutaImagePickerFeatureStrings,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaImagePickerFeature] instances.
     *
     * This builder ensures all required sub-features are configured before
     * creating the final image picker feature configuration.
     */
    public class Builder : AiutaFeature.Builder {
        /**
         * Optional camera capture feature configuration.
         */
        public var camera: AiutaImagePickerCameraFeature? = null

        /**
         * Required photo gallery selection feature configuration.
         */
        public var photoGallery: AiutaImagePickerPhotoGalleryFeature? = null

        /**
         * Optional predefined model images feature configuration.
         */
        public var predefinedModels: AiutaImagePickerPredefinedModelFeature? = null

        /**
         * Optional upload history feature configuration.
         */
        public var uploadsHistory: AiutaImagePickerUploadsHistoryFeature? = null

        /**
         * Optional protection disclaimer feature configuration.
         */
        public var protectionDisclaimer: AiutaImagePickerProtectionDisclaimerFeature? = null

        /**
         * Required image resources and placeholders configuration.
         */
        public var images: AiutaImagePickerFeatureImages? = null

        /**
         * Required text content and localization configuration.
         */
        public var strings: AiutaImagePickerFeatureStrings? = null

        /**
         * Creates an [AiutaImagePickerFeature] instance with the configured properties.
         *
         * @return Configured [AiutaImagePickerFeature] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public override fun build(): AiutaImagePickerFeature {
            val parentClass = "AiutaImagePickerFeature"

            return AiutaImagePickerFeature(
                camera = camera,
                photoGallery = photoGallery.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "photoGallery",
                ),
                predefinedModels = predefinedModels,
                uploadsHistory = uploadsHistory,
                protectionDisclaimer = protectionDisclaimer,
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
 * Extension function for configuring image picker within an [AiutaFeatures.Builder].
 *
 * This extension provides a convenient DSL for configuring the image picker
 * feature as part of the main features setup.
 *
 * ```kotlin
 * imagePicker {
 *      photoGallery {
 *          // Configure photo gallery settings
 *      }
 *      images = ...
 *      strings = ...
 *
 *      // Optional features
 *      camera {
 *          // Configure camera settings
 *      }
 *      predefinedModels {
 *          // Configure predefined models
 *      }
 * }
 * ```
 *
 * @param block Configuration block for setting up the image picker feature
 * @return The features builder for method chaining
 * @see AiutaFeatures.Builder
 * @see AiutaImagePickerFeature.Builder
 */
public inline fun AiutaFeatures.Builder.imagePicker(
    block: AiutaImagePickerFeature.Builder.() -> Unit,
): AiutaFeatures.Builder = apply {
    imagePicker = AiutaImagePickerFeature.Builder().apply(block).build()
}
