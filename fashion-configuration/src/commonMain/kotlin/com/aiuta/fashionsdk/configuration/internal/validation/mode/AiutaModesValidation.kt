package com.aiuta.fashionsdk.configuration.internal.validation.mode

import com.aiuta.fashionsdk.configuration.debug.AiutaDebugSettings
import com.aiuta.fashionsdk.configuration.internal.validation.validateMapWithSettings
import com.aiuta.fashionsdk.configuration.internal.validation.validateStringWithSettings
import com.aiuta.fashionsdk.configuration.mode.AiutaModes
import com.aiuta.fashionsdk.logger.AiutaLogger

internal fun AiutaModes.validateWithSettings(
    logger: AiutaLogger?,
    debugSettings: AiutaDebugSettings,
) {
    // Shoes mode
    // Onboarding strings
    shoes?.onboardingShoesPage?.strings?.onboardingShoesBestResultsPageTitle.validateStringWithSettings(
        propertyName = "onboardingShoesBestResultsPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    shoes?.onboardingShoesPage?.strings?.onboardingShoesBestResultsTitle.validateStringWithSettings(
        propertyName = "onboardingShoesBestResultsTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    shoes?.onboardingShoesPage?.strings?.onboardingShoesBestResultsDescription.validateStringWithSettings(
        propertyName = "onboardingShoesBestResultsDescription",
        logger = logger,
        debugSettings = debugSettings,
    )

    // Image picker predefined models
    shoes?.imagePicker?.predefinedModels?.strings?.predefinedModelShoesCategories.validateMapWithSettings(
        propertyName = "predefinedModelShoesCategories",
        logger = logger,
        debugSettings = debugSettings,
    )

    // Image picker strings
    shoes?.imagePicker?.strings?.imagePickerShoesDescriptionEmpty.validateStringWithSettings(
        propertyName = "imagePickerShoesDescriptionEmpty",
        logger = logger,
        debugSettings = debugSettings,
    )
}
