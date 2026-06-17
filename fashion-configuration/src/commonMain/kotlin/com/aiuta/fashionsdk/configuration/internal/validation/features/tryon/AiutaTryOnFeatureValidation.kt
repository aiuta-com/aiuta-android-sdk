package com.aiuta.fashionsdk.configuration.internal.validation.features.tryon

import com.aiuta.fashionsdk.configuration.debug.AiutaDebugSettings
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.internal.validation.validateListWithSettings
import com.aiuta.fashionsdk.configuration.internal.validation.validateStringWithSettings
import com.aiuta.fashionsdk.logger.AiutaLogger

internal fun AiutaTryOnFeature.validateWithSettings(
    logger: AiutaLogger?,
    debugSettings: AiutaDebugSettings,
) {
    // Strings
    // Features
    loadingPage.strings.tryOnLoadingStatusUploadingImage.validateStringWithSettings(
        propertyName = "tryOnLoadingStatusUploadingImage",
        logger = logger,
        debugSettings = debugSettings,
    )
    loadingPage.strings.tryOnLoadingStatusScanningBody.validateStringWithSettings(
        propertyName = "tryOnLoadingStatusScanningBody",
        logger = logger,
        debugSettings = debugSettings,
    )
    loadingPage.strings.tryOnLoadingStatusGeneratingOutfit.validateStringWithSettings(
        propertyName = "tryOnLoadingStatusGeneratingOutfit",
        logger = logger,
        debugSettings = debugSettings,
    )

    fitDisclaimer?.strings?.tryOnFitTitle.validateStringWithSettings(
        propertyName = "tryOnFitTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    fitDisclaimer?.strings?.tryOnFitDescription.validateStringWithSettings(
        propertyName = "tryOnFitDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    fitDisclaimer?.strings?.tryOnFitButtonClose.validateStringWithSettings(
        propertyName = "tryOnFitButtonClose",
        logger = logger,
        debugSettings = debugSettings,
    )

    feedback?.strings?.tryOnFeedbackOptions?.forEachIndexed { index, string ->
        string.validateStringWithSettings(
            propertyName = "tryOnFeedbackOptions[$index]",
            logger = logger,
            debugSettings = debugSettings,
        )
    }
    feedback?.strings?.tryOnFeedbackTitle.validateStringWithSettings(
        propertyName = "tryOnFeedbackTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.strings?.tryOnFeedbackButtonSkip.validateStringWithSettings(
        propertyName = "tryOnFeedbackButtonSkip",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.strings?.tryOnFeedbackButtonSend.validateStringWithSettings(
        propertyName = "tryOnFeedbackButtonSend",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.strings?.tryOnFeedbackGratitudeText.validateStringWithSettings(
        propertyName = "tryOnFeedbackGratitudeText",
        logger = logger,
        debugSettings = debugSettings,
    )

    feedback?.otherFeedback?.strings?.otherFeedbackTitle.validateStringWithSettings(
        propertyName = "otherFeedbackTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.otherFeedback?.strings?.otherFeedbackButtonSend.validateStringWithSettings(
        propertyName = "otherFeedbackButtonSend",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.otherFeedback?.strings?.otherFeedbackButtonCancel.validateStringWithSettings(
        propertyName = "otherFeedbackButtonCancel",
        logger = logger,
        debugSettings = debugSettings,
    )
    feedback?.otherFeedback?.strings?.otherFeedbackOptionOther.validateStringWithSettings(
        propertyName = "otherFeedbackOptionOther",
        logger = logger,
        debugSettings = debugSettings,
    )

    generationsHistory?.strings?.generationsHistoryPageTitle.validateStringWithSettings(
        propertyName = "generationsHistoryPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )

    // Cart
    cart.strings.addToCart.validateStringWithSettings(
        propertyName = "addToCart",
        logger = logger,
        debugSettings = debugSettings,
    )

    // Input validation
    inputImageValidation.strings.invalidInputImageDescription.validateStringWithSettings(
        propertyName = "invalidInputImageDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.invalidInputImageChangePhotoButton.validateStringWithSettings(
        propertyName = "invalidInputImageChangePhotoButton",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.noPeopleDetectedDescription.validateStringWithSettings(
        propertyName = "noPeopleDetectedDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.tooManyPeopleDetectedDescription.validateStringWithSettings(
        propertyName = "tooManyPeopleDetectedDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.childDetectedDescription.validateStringWithSettings(
        propertyName = "childDetectedDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.insufficientTargetAreaDescription.validateStringWithSettings(
        propertyName = "insufficientTargetAreaDescription",
        logger = logger,
        debugSettings = debugSettings,
    )
    inputImageValidation.strings.internalRestrictionDescription.validateStringWithSettings(
        propertyName = "internalRestrictionDescription",
        logger = logger,
        debugSettings = debugSettings,
    )

    // General
    strings.tryOnPageTitle.validateStringWithSettings(
        propertyName = "tryOnPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    strings.tryOn.validateStringWithSettings(
        propertyName = "tryOn",
        logger = logger,
        debugSettings = debugSettings,
    )

    // Lists
    loadingPage.styles.loadingStatusBackgroundGradient?.validateListWithSettings(
        propertyName = "loadingStatusBackgroundGradient",
        logger = logger,
        debugSettings = debugSettings,
    )

    feedback?.strings?.tryOnFeedbackOptions.validateListWithSettings(
        propertyName = "tryOnFeedbackOptions",
        logger = logger,
        debugSettings = debugSettings,
    )

    styles.tryOnButtonGradient?.validateListWithSettings(
        propertyName = "tryOnButtonGradient",
        logger = logger,
        debugSettings = debugSettings,
    )
}
