package com.aiuta.fashionsdk.configuration.defaults.features.onboarding

import com.aiuta.fashionsdk.configuration.defaults.images.features.onboarding.bestresult.DefaultsAiutaOnboardingBestResultsPageFeatureImages
import com.aiuta.fashionsdk.configuration.defaults.images.features.onboarding.tryon.DefaultAiutaOnboardingHowItWorksPageFeatureImages
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.bestResultsPage
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.strings.AiutaOnboardingBestResultsPageFeatureStrings
import com.aiuta.fashionsdk.configuration.features.onboarding.dataprovider.AiutaOnboardingFeatureDataProviderBuiltIn
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.howItWorksPage
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.strings.AiutaOnboardingHowItWorksPageFeatureStrings
import com.aiuta.fashionsdk.configuration.features.onboarding.onboarding
import com.aiuta.fashionsdk.configuration.features.onboarding.strings.AiutaOnboardingFeatureStrings

/**
 * Configures the default onboarding feature for the Aiuta SDK.
 *
 * This function sets up the onboarding feature with default best results page, how it works page, strings, shapes, and data provider.
 *
 * @return The updated [AiutaFeatures.Builder] instance.
 */
public fun AiutaFeatures.Builder.defaultOnboarding(): AiutaFeatures.Builder = onboarding {
    bestResultsPage {
        images = DefaultsAiutaOnboardingBestResultsPageFeatureImages()
        strings = AiutaOnboardingBestResultsPageFeatureStrings.Default()
    }
    howItWorksPage {
        images = DefaultAiutaOnboardingHowItWorksPageFeatureImages()
        strings = AiutaOnboardingHowItWorksPageFeatureStrings.Default()
    }
    strings = AiutaOnboardingFeatureStrings.Default()
    dataProvider = AiutaOnboardingFeatureDataProviderBuiltIn
}
