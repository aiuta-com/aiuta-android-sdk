package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId

internal sealed interface OnboardingScreenAction {

    // Pager scaffold control (the PagerState lives in the composable)
    data class ScrollToPage(val page: Int) : OnboardingScreenAction

    // Navigation
    data object NavigateBack : OnboardingScreenAction
    data object NavigateToImageSelector : OnboardingScreenAction
    data class Close(val pageId: AiutaAnalyticsPageId) : OnboardingScreenAction
}
