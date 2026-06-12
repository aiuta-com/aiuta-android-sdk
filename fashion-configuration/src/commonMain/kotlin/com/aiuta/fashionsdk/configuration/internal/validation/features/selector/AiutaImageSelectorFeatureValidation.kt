package com.aiuta.fashionsdk.configuration.internal.validation.features.selector

import com.aiuta.fashionsdk.configuration.debug.AiutaDebugSettings
import com.aiuta.fashionsdk.configuration.features.picker.AiutaImagePickerFeature
import com.aiuta.fashionsdk.configuration.internal.validation.validateMapWithSettings
import com.aiuta.fashionsdk.configuration.internal.validation.validateStringWithSettings
import com.aiuta.fashionsdk.logger.AiutaLogger

internal fun AiutaImagePickerFeature.validateWithSettings(
    logger: AiutaLogger?,
    debugSettings: AiutaDebugSettings,
) {
    // Strings
    // Features
    camera?.strings?.cameraButtonTakePhoto.validateStringWithSettings(
        propertyName = "cameraButtonTakePhoto",
        logger = logger,
        debugSettings = debugSettings,
    )
    camera?.strings?.cameraPermissionTitle.validateStringWithSettings(
        propertyName = "cameraPermissionTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    camera?.strings?.cameraPermissionDescription.validateStringWithSettings(
        propertyName = "cameraPermissionDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    camera?.strings?.cameraPermissionButtonOpenSettings.validateStringWithSettings(
        propertyName = "cameraPermissionButtonOpenSettings",
        logger = logger,
        debugSettings = debugSettings,
    )

    photoGallery.strings.galleryButtonSelectPhoto.validateStringWithSettings(
        "galleryButtonSelectPhoto",
        logger,
        debugSettings,
    )

    predefinedModels?.data?.preferredCategoryId.validateStringWithSettings(
        propertyName = "preferredCategoryId",
        logger = logger,
        debugSettings = debugSettings,
    )
    predefinedModels?.strings?.predefinedModelPageTitle.validateStringWithSettings(
        propertyName = "predefinedModelPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    predefinedModels?.strings?.predefinedModelOr.validateStringWithSettings(
        propertyName = "predefinedModelOr",
        logger = logger,
        debugSettings = debugSettings,
    )
    predefinedModels?.strings?.predefinedModelErrorEmptyModelsList.validateStringWithSettings(
        propertyName = "predefinedModelErrorEmptyModelsList",
        logger = logger,
        debugSettings = debugSettings,
    )
    predefinedModels?.strings?.predefinedModelCategories.validateMapWithSettings(
        "predefinedModelCategories",
        logger,
        debugSettings,
    )

    uploadsHistory?.strings?.uploadsHistoryTitle.validateStringWithSettings(
        propertyName = "uploadsHistoryTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    uploadsHistory?.strings?.uploadsHistoryButtonNewPhoto.validateStringWithSettings(
        propertyName = "uploadsHistoryButtonNewPhoto",
        logger = logger,
        debugSettings = debugSettings,
    )
    uploadsHistory?.strings?.uploadsHistoryButtonChangePhoto.validateStringWithSettings(
        propertyName = "uploadsHistoryButtonChangePhoto",
        logger = logger,
        debugSettings = debugSettings,
    )

    // General
    strings.imagePickerTitleEmpty.validateStringWithSettings(
        propertyName = "imagePickerTitleEmpty",
        logger = logger,
        debugSettings = debugSettings,
    )
    strings.imagePickerDescriptionEmpty.validateStringWithSettings(
        propertyName = "imagePickerDescriptionEmpty",
        logger = logger,
        debugSettings = debugSettings,
    )
    strings.imagePickerButtonUploadImage.validateStringWithSettings(
        propertyName = "imagePickerButtonUploadImage",
        logger = logger,
        debugSettings = debugSettings,
    )
}
