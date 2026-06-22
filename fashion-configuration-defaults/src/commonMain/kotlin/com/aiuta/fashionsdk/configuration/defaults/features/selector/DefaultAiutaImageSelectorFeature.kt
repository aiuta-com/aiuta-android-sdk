package com.aiuta.fashionsdk.configuration.defaults.features.selector

import com.aiuta.fashionsdk.configuration.defaults.icons.features.selector.camera.DefaultAiutaImagePickerCameraFeatureIcons
import com.aiuta.fashionsdk.configuration.defaults.icons.features.selector.gallery.DefaultAiutaImagePickerPhotoGalleryFeatureIcons
import com.aiuta.fashionsdk.configuration.defaults.icons.features.selector.model.DefaultAiutaImagePickerPredefinedModelFeatureIcons
import com.aiuta.fashionsdk.configuration.defaults.images.features.selector.DefaultAiutaImagePickerFeatureImages
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.picker.camera.camera
import com.aiuta.fashionsdk.configuration.features.picker.camera.strings.AiutaImagePickerCameraFeatureStrings
import com.aiuta.fashionsdk.configuration.features.picker.gallery.photoGallery
import com.aiuta.fashionsdk.configuration.features.picker.gallery.strings.AiutaImagePickerPhotoGalleryFeatureStrings
import com.aiuta.fashionsdk.configuration.features.picker.history.dataprovider.AiutaImagePickerUploadsHistoryFeatureDataProviderBuiltIn
import com.aiuta.fashionsdk.configuration.features.picker.history.strings.AiutaImagePickerUploadsHistoryFeatureStrings
import com.aiuta.fashionsdk.configuration.features.picker.history.uploadsHistory
import com.aiuta.fashionsdk.configuration.features.picker.imagePicker
import com.aiuta.fashionsdk.configuration.features.picker.model.predefinedModels
import com.aiuta.fashionsdk.configuration.features.picker.model.strings.AiutaImagePickerPredefinedModelFeatureStrings
import com.aiuta.fashionsdk.configuration.features.picker.strings.AiutaImagePickerFeatureStrings

/**
 * Configures the default image selector feature for the Aiuta SDK.
 *
 * This function sets up the image selector with default camera, photo gallery, predefined models, uploads history, images, and strings.
 *
 * @return The updated [AiutaFeatures.Builder] instance.
 */
public fun AiutaFeatures.Builder.defaultImagePicker(): AiutaFeatures.Builder = imagePicker {
    camera {
        icons = DefaultAiutaImagePickerCameraFeatureIcons()
        strings = AiutaImagePickerCameraFeatureStrings.Default()
    }
    photoGallery {
        icons = DefaultAiutaImagePickerPhotoGalleryFeatureIcons()
        strings = AiutaImagePickerPhotoGalleryFeatureStrings.Default()
    }
    predefinedModels {
        icons = DefaultAiutaImagePickerPredefinedModelFeatureIcons()
        strings = AiutaImagePickerPredefinedModelFeatureStrings.Default()
    }
    uploadsHistory {
        strings = AiutaImagePickerUploadsHistoryFeatureStrings.Default()
        dataProvider = AiutaImagePickerUploadsHistoryFeatureDataProviderBuiltIn
    }
    images = DefaultAiutaImagePickerFeatureImages()
    strings = AiutaImagePickerFeatureStrings.Default()
}
