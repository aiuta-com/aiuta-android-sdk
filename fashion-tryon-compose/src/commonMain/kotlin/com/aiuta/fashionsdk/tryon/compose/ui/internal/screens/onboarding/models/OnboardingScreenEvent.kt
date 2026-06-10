package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel

internal sealed interface OnboardingScreenEvent {

    // App bar / navigation
    data class BackClicked(val settledPage: Int) : OnboardingScreenEvent
    data class CloseClicked(val pageId: AiutaAnalyticsPageId) : OnboardingScreenEvent

    // Primary button
    data class NextClicked(val settledPage: Int) : OnboardingScreenEvent

    // Try on page
    data class InternalTryOnPageClicked(val index: Int) : OnboardingScreenEvent

    // Consent page
    data class ConsentToggled(
        val consent: AiutaConsentUiModel,
        val isObtained: Boolean,
    ) : OnboardingScreenEvent
}
