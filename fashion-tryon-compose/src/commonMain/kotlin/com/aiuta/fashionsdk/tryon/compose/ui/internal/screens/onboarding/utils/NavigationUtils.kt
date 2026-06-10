package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils

import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.OnboardingViewModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage

/**
 * Mirrors the old `OnboardingController.nextPage`. The flattened pager index ([settledPage]) is
 * genuine scaffold state owned by the composable and is passed in via the event; the view model
 * owns the logical step + queue and decides what should happen next.
 */
internal fun OnboardingViewModel.navigateNextPage(settledPage: Int) {
    val state = viewState.value
    val currentStep = state.currentStep
    val nextPageIndex = settledPage + 1
    val nextStep = state.onboardingStatesQueue.getOrNull(
        index = (nextPageIndex - TryOnPage.INTERNAL_PAGES_LAST_INDEX).coerceAtLeast(0),
    )

    if (nextStep != null) {
        val isLastPageOfTryOn = settledPage == TryOnPage.INTERNAL_PAGES_LAST_INDEX

        // Skip step change while still inside the Try on page internal pages
        if (currentStep !is TryOnPage || isLastPageOfTryOn) {
            changeStep(nextStep)
        }

        emitAction(OnboardingScreenAction.ScrollToPage(nextPageIndex))
    } else {
        completeOnboarding()
    }
}

/**
 * Mirrors the old `OnboardingController.previousPage`.
 */
internal fun OnboardingViewModel.navigatePreviousPage(settledPage: Int) {
    val state = viewState.value
    val currentStep = state.currentStep
    val previousPageIndex = settledPage - 1
    val isFirstPage = settledPage == 0

    val previousStep = state.onboardingStatesQueue.getOrNull(
        index = (previousPageIndex - TryOnPage.INTERNAL_PAGES_LAST_INDEX).coerceAtLeast(0),
    ).takeIf { !isFirstPage }

    if (previousStep != null) {
        // Skip step change for the Try on page case
        if (currentStep !is TryOnPage) {
            changeStep(previousStep)
        }

        emitAction(OnboardingScreenAction.ScrollToPage(previousPageIndex))
    } else {
        emitAction(OnboardingScreenAction.NavigateBack)
    }
}
