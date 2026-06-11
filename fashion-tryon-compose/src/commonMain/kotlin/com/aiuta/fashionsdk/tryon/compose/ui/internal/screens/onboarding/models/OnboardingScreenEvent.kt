package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel

internal sealed interface OnboardingScreenEvent {

    // App bar / navigation
    data object BackClicked : OnboardingScreenEvent
    data class CloseClicked(val pageId: AiutaAnalyticsPageId) : OnboardingScreenEvent

    // Primary button
    data object NextClicked : OnboardingScreenEvent

    // Consent page
    data class ConsentToggled(
        val consent: AiutaConsentUiModel,
        val isObtained: Boolean,
    ) : OnboardingScreenEvent
}
