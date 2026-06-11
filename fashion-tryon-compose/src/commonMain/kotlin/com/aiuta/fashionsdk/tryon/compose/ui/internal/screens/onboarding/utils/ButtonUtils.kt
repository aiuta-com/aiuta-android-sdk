package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenViewState

/**
 * Mirrors the old primary button text resolution. The current internal Try on page is genuine
 * pager scaffold state
 */
@Composable
internal fun solvePrimaryButtonText(
    viewState: State<OnboardingScreenViewState>,
    onboardingFeature: AiutaOnboardingFeature,
    consentStandaloneOnboardingFeature: AiutaConsentStandaloneOnboardingPageFeature?,
): String {
    val currentStep = viewState.value.currentStep
    val onboardingStatesQueue = viewState.value.onboardingStatesQueue

    return when {
        currentStep != onboardingStatesQueue.last() -> onboardingFeature.strings.onboardingButtonNext

        currentStep is ConsentPage && consentStandaloneOnboardingFeature != null -> {
            consentStandaloneOnboardingFeature.strings.consentButtonAccept
        }

        else -> onboardingFeature.strings.onboardingButtonStart
    }
}
