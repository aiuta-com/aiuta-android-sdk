package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel

@Immutable
internal data class OnboardingScreenViewState(
    val onboardingStatesQueue: List<OnboardingStep>,
    // Null only while the per-mode completion state is being loaded, or when there are no slides to
    // show (guarded by the Splash flow, which won't navigate here in that case).
    val currentStep: OnboardingStep?,
    val consents: List<AiutaConsentUiModel> = emptyList(),
    val isPrimaryButtonEnabled: Boolean = true,
)
