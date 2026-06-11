package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature

@Immutable
internal sealed interface OnboardingStep {
    val pageTitle: String?
}

internal class TryOnPage(
    tryOnPageFeature: AiutaOnboardingHowItWorksPageFeature,
) : OnboardingStep {

    override val pageTitle: String? = tryOnPageFeature.strings.onboardingHowItWorksPageTitle

    val video: AiutaMedia? = tryOnPageFeature.images?.onboardingHowItWorksItem
}

internal class BestResultPage(
    bestResultsPageFeature: AiutaOnboardingBestResultsPageFeature,
) : OnboardingStep {
    override val pageTitle: String? = bestResultsPageFeature.strings.onboardingBestResultsPageTitle

    val goodImages: List<AiutaDrawableResource> =
        bestResultsPageFeature.images.onboardingBestResultsGood
    val badImages: List<AiutaDrawableResource> =
        bestResultsPageFeature.images.onboardingBestResultsBad
}

internal class ConsentPage(
    consentStandaloneFeature: AiutaConsentStandaloneOnboardingPageFeature,
) : OnboardingStep {
    override val pageTitle: String? = consentStandaloneFeature.strings.consentPageTitle
}
