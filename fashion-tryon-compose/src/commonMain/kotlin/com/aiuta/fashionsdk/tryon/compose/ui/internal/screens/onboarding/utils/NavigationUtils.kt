package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils

import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.OnboardingViewModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenAction

/**
 * Mirrors the old `OnboardingController.nextPage`. The flattened pager index (`settledPage`) is
 * genuine scaffold state owned by the composable and is passed in via the event; the view model
 * owns the logical step + queue and decides what should happen next.
 */
internal fun OnboardingViewModel.navigateNextPage() {
    val state = viewState.value

    val currentStep = state.currentStep

    // Record per-slide completion for the slide we are leaving before advancing.
    currentStep?.let { recordCompletionOnLeaving(it) }

    val currentStepIndex = state.onboardingStatesQueue.indexOf(currentStep)

    val nextPageIndex = currentStepIndex + 1
    val nextStep = state.onboardingStatesQueue.getOrNull(nextPageIndex)

    if (nextStep != null) {
        changeStep(nextStep)
    } else {
        finishOnboarding()
    }
}

/**
 * Mirrors the old `OnboardingController.previousPage`.
 */
internal fun OnboardingViewModel.navigatePreviousPage() {
    val state = viewState.value

    val currentStep = state.currentStep
    val currentStepIndex = state.onboardingStatesQueue.indexOf(currentStep)

    val prevStepIndex = currentStepIndex - 1
    val previousStep = state.onboardingStatesQueue.getOrNull(prevStepIndex)

    if (previousStep != null) {
        changeStep(previousStep)
    } else {
        emitAction(OnboardingScreenAction.NavigateBack)
    }
}
