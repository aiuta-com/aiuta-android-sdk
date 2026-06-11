package com.aiuta.fashionsdk.configuration.internal.validation.features.onboarding

import com.aiuta.fashionsdk.configuration.debug.AiutaDebugSettings
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.internal.validation.validateStringWithSettings
import com.aiuta.fashionsdk.logger.AiutaLogger

internal fun AiutaOnboardingFeature.validateWithSettings(
    logger: AiutaLogger?,
    debugSettings: AiutaDebugSettings,
) {
    // Strings
    // Features
    howItWorksPage.strings.onboardingHowItWorksPageTitle.validateStringWithSettings(
        propertyName = "onboardingHowItWorksPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    howItWorksPage.strings.onboardingHowItWorksTitle.validateStringWithSettings(
        propertyName = "onboardingHowItWorksTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    howItWorksPage.strings.onboardingHowItWorksDescription.validateStringWithSettings(
        propertyName = "onboardingHowItWorksDescription",
        logger = logger,
        debugSettings = debugSettings,
    )

    bestResultsPage?.strings?.onboardingBestResultsPageTitle.validateStringWithSettings(
        propertyName = "onboardingBestResultsPageTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    bestResultsPage?.strings?.onboardingBestResultsTitle.validateStringWithSettings(
        propertyName = "onboardingBestResultsTitle",
        logger = logger,
        debugSettings = debugSettings,
    )
    bestResultsPage?.strings?.onboardingBestResultsDescription.validateStringWithSettings(
        propertyName = "onboardingBestResultsDescription",
        logger = logger,
        debugSettings = debugSettings,
    )

    // General
    strings.onboardingButtonNext.validateStringWithSettings(
        propertyName = "onboardingButtonNext",
        logger = logger,
        debugSettings = debugSettings,
    )
    strings.onboardingButtonStart.validateStringWithSettings(
        propertyName = "onboardingButtonStart",
        logger = logger,
        debugSettings = debugSettings,
    )
}
