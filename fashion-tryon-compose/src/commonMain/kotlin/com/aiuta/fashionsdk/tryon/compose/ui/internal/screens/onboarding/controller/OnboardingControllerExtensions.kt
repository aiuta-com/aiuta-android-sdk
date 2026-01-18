package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendOnboardingEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.isAllMandatoryConsentChecked
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import kotlinx.coroutines.launch

internal fun OnboardingController.nextPage(
    controller: FashionTryOnController,
    navigationController: AiutaNavigationController,
) {
    scope.launch {
        val nextPageIndex = pagerState.settledPage + 1
        val nextState = onboardingStatesQueue.getOrNull(
            index = (nextPageIndex - TryOnPage.INTERNAL_PAGES_LAST_INDEX).coerceAtLeast(0),
        )

        if (nextState != null) {
            val isLastPageOfTryOn = pagerState.settledPage == TryOnPage.INTERNAL_PAGES_LAST_INDEX

            // Skip for Try on page case
            if (state.value !is TryOnPage || isLastPageOfTryOn) {
                state.value = nextState
            }

            pagerState.animateScrollToPage(nextPageIndex)
        } else {
            // Close onboarding and move on
            val consentsList = consentController?.consentsList
            val obtainedConsentId = consentsList?.mapNotNull { consentModel ->
                consentModel.consent.id.takeIf { consentModel.isObtained }
            }.orEmpty()

            // Save or notify host as completed onboarding
            controller.onboardingInteractor.completeOnboarding()

            // Consent
            controller.sendOnboardingEvent(
                eventType = AiutaAnalyticOnboardingEventType.CONSENT_GIVEN,
                pageId = AiutaAnalyticsPageId.CONSENT,
                consentsIds = consentsList?.map { consent -> consent.consent.id },
            )
            controller.consentInteractor::obtainConsent.safeInvoke(obtainedConsentId)

            // Finish
            controller.sendOnboardingEvent(
                eventType = AiutaAnalyticOnboardingEventType.ONBOARDING_FINISHED,
                pageId = AiutaAnalyticsPageId.CONSENT,
                consentsIds = null,
            )
            navigationController.popUpAndNavigateTo(TryOnScreen.ImageSelector)
        }
    }
}

internal fun OnboardingController.previousPage(controller: FashionTryOnController) {
    scope.launch {
        val previousPageIndex = pagerState.settledPage - 1
        val isFirstPage = pagerState.settledPage == 0

        val previousState = onboardingStatesQueue.getOrNull(
            index = (previousPageIndex - TryOnPage.INTERNAL_PAGES_LAST_INDEX).coerceAtLeast(0),
        ).takeIf { !isFirstPage }

        if (previousState != null) {
            // Skip for Try on page case
            if (state.value !is TryOnPage) {
                state.value = previousState
            }

            pagerState.animateScrollToPage(previousPageIndex)
        } else {
            // Try to navigate back
            controller.navigateBack()
        }
    }
}

internal fun OnboardingController.changeInternalTryOnPage(newPage: Int) {
    scope.launch {
        pagerState.animateScrollToPage(newPage)
    }
}

// Agreement
@Composable
internal fun OnboardingController.listenIsPrimaryButtonEnabled(): State<Boolean> = remember(
    state.value,
    consentController?.consentsList,
) {
    derivedStateOf {
        val isNotConsentPage = state.value !is ConsentPage
        val isAllMandatoryChecked = consentController?.isAllMandatoryConsentChecked()
        val isCompletedAllMandatoryChecked = isAllMandatoryChecked == null || isAllMandatoryChecked == true

        isNotConsentPage || isCompletedAllMandatoryChecked
    }
}
