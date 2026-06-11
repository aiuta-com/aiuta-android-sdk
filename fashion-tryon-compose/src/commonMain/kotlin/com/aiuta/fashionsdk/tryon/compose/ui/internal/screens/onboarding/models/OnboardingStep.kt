package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature

@Immutable
internal sealed interface OnboardingStep {
    val pageTitle: String?

    fun pageSize(): Int = 1
}

internal class TryOnPage(
    tryOnPageFeature: AiutaOnboardingHowItWorksPageFeature,
) : OnboardingStep {

    override val pageTitle: String? = tryOnPageFeature.strings.onboardingHowItWorksPageTitle

    // TODO Add video file?

    companion object {
        const val INTERNAL_PAGES_SIZE = 3
        const val INTERNAL_PAGES_LAST_INDEX = INTERNAL_PAGES_SIZE - 1
    }
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
