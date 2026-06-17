package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils

import com.aiuta.fashionsdk.configuration.mode.AiutaMode

/**
 * Single source of truth for deciding which onboarding slides to show, shared by both the
 * Splash flow (to decide whether to enter onboarding at all) and the onboarding screen (to build
 * its slide queue). Keeping the rules here avoids the two diverging.
 *
 * @param mode Active try-on mode for the current session.
 * @param isOnboardingEnabled Whether the onboarding feature is configured at all.
 * @param isHowItWorksEnabled Whether the "how it works" slide is configured.
 * @param isGeneralBestResultsEnabled Whether the general "best results" slide is configured.
 * @param isShoesBestResultsEnabled Whether the shoes "best results" slide is configured
 *   (i.e. `modes.shoes?.onboardingShoesPage != null`).
 * @param completion Per-mode completion map; a mode absent or mapped to `false` is not completed.
 */
internal class OnboardingResolutionInput(
    val mode: AiutaMode,
    val isOnboardingEnabled: Boolean,
    val isHowItWorksEnabled: Boolean,
    val isGeneralBestResultsEnabled: Boolean,
    val isShoesBestResultsEnabled: Boolean,
    val completion: Map<AiutaMode, Boolean>,
) {

    private fun isCompleted(mode: AiutaMode): Boolean = completion[mode] == true

    private fun isAnyCompleted(): Boolean = completion.values.any { it }

    /**
     * General onboarding enabled AND how-it-works enabled AND the user has not completed ANY
     * onboarding yet (once any onboarding is completed, this slide has been seen).
     */
    fun showHowItWorks(): Boolean = isOnboardingEnabled &&
        isHowItWorksEnabled &&
        !isAnyCompleted()

    /**
     * Mode is GENERAL AND general onboarding enabled AND general best-results enabled AND general
     * onboarding not completed.
     */
    fun showGeneralBestResults(): Boolean = mode == AiutaMode.GENERAL &&
        isOnboardingEnabled &&
        isGeneralBestResultsEnabled &&
        !isCompleted(AiutaMode.GENERAL)

    /**
     * Mode is SHOES AND shoes best-results enabled AND shoes onboarding not completed.
     *
     * Also gated on [isOnboardingEnabled]: the slide is hosted by the onboarding screen, which
     * requires the onboarding feature (it also provides the completion data provider).
     */
    fun showShoesBestResults(): Boolean = mode == AiutaMode.SHOES &&
        isOnboardingEnabled &&
        isShoesBestResultsEnabled &&
        !isCompleted(AiutaMode.SHOES)

    fun anySlideToShow(): Boolean = showHowItWorks() || showGeneralBestResults() || showShoesBestResults()
}
