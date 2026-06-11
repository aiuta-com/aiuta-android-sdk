package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel

@Immutable
internal data class OnboardingScreenViewState(
    val onboardingStatesQueue: List<OnboardingStep>,
    val currentStep: OnboardingStep,
    val consents: List<AiutaConsentUiModel> = emptyList(),
    val isPrimaryButtonEnabled: Boolean = true,
)
