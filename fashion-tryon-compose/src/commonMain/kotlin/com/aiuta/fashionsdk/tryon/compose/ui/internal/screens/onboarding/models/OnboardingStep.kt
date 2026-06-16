package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.AiutaShoesModeOnboardingPage

@Immutable
internal sealed interface OnboardingStep {
    val pageTitle: String?
}

internal class TryOnPage(
    tryOnPageFeature: AiutaOnboardingHowItWorksPageFeature,
) : OnboardingStep {

    override val pageTitle: String? = tryOnPageFeature.strings.onboardingHowItWorksPageTitle

    val mediaItem: AiutaMedia = tryOnPageFeature.images.onboardingHowItWorksItem
}

internal class BestResultPage(
    bestResultsPageFeature: AiutaOnboardingBestResultsPageFeature,
) : OnboardingStep {
    override val pageTitle: String? = bestResultsPageFeature.strings.onboardingBestResultsPageTitle

    val mediaItem: AiutaMedia = bestResultsPageFeature.images.onboardingBestResultsItem
}

/**
 * Shoes-mode "best results" slide. Uses the same layout as [BestResultPage], but its content comes
 * from [AiutaShoesModeOnboardingPage] (shoes-mode config) rather than the registered features, so the
 * values are held directly on the step instead of being resolved via `strictProvideFeature`.
 */
internal class ShoesBestResultPage(
    onboardingShoesPage: AiutaShoesModeOnboardingPage,
) : OnboardingStep {
    override val pageTitle: String? = onboardingShoesPage.strings.onboardingShoesBestResultsPageTitle

    val title: String = onboardingShoesPage.strings.onboardingShoesBestResultsTitle
    val subtitle: String = onboardingShoesPage.strings.onboardingShoesBestResultsDescription
    val mediaItem: AiutaMedia = onboardingShoesPage.images.onboardingShoesBestResultsItem
}

internal class ConsentPage(
    consentStandaloneFeature: AiutaConsentStandaloneOnboardingPageFeature,
) : OnboardingStep {
    override val pageTitle: String? = consentStandaloneFeature.strings.consentPageTitle
}
