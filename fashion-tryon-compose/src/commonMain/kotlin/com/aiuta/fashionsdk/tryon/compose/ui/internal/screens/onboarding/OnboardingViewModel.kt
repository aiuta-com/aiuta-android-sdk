package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.consent.ConsentInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.onboarding.OnboardingInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.utils.isRequired
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendOnboardingEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.toUiModels
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.BestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenViewState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingStep
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.navigateNextPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.navigatePreviousPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class OnboardingViewModel(
    private val features: AiutaFeatures,
    private val controller: FashionTryOnController,
    private val onboardingInteractor: OnboardingInteractor,
    private val consentInteractor: ConsentInteractor,
) : ViewModel() {

    private val _viewState = MutableStateFlow(buildInitialState())
    val viewState = _viewState.asStateFlow()

    private val _viewAction = MutableStateFlow<OnboardingScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    fun obtainEvent(event: OnboardingScreenEvent) {
        when (event) {
            is OnboardingScreenEvent.BackClicked -> navigatePreviousPage(event.settledPage)
            is OnboardingScreenEvent.CloseClicked -> {
                _viewAction.update { OnboardingScreenAction.Close(event.pageId) }
            }

            is OnboardingScreenEvent.NextClicked -> navigateNextPage(event.settledPage)
            is OnboardingScreenEvent.InternalTryOnPageClicked -> {
                _viewAction.update { OnboardingScreenAction.ScrollToPage(event.index) }
            }

            is OnboardingScreenEvent.ConsentToggled -> toggleConsent(event.consent, event.isObtained)
        }
    }

    fun clearAction() {
        _viewAction.value = null
    }

    internal fun emitAction(action: OnboardingScreenAction) {
        _viewAction.update { action }
    }

    internal fun changeStep(step: OnboardingStep) {
        _viewState.update { state ->
            state.copy(
                currentStep = step,
                isPrimaryButtonEnabled = solveIsPrimaryButtonEnabled(
                    currentStep = step,
                    consents = state.consents,
                ),
            )
        }
    }

    internal fun completeOnboarding() {
        viewModelScope.launch {
            // Save or notify host as completed onboarding
            onboardingInteractor.completeOnboarding()

            // Consent
            completeConsentViewing()

            // Finish
            controller.sendOnboardingEvent(
                eventType = AiutaAnalyticOnboardingEventType.ONBOARDING_FINISHED,
                pageId = AiutaAnalyticsPageId.CONSENT,
                consentsIds = null,
            )

            _viewAction.update { OnboardingScreenAction.NavigateToImageSelector }
        }
    }

    private fun toggleConsent(consent: AiutaConsentUiModel, isObtained: Boolean) {
        _viewState.update { state ->
            val updatedConsents = state.consents.map { consentModel ->
                if (consentModel.consent.id == consent.consent.id) {
                    consentModel.copy(isObtained = isObtained)
                } else {
                    consentModel
                }
            }

            state.copy(
                consents = updatedConsents,
                isPrimaryButtonEnabled = solveIsPrimaryButtonEnabled(
                    currentStep = state.currentStep,
                    consents = updatedConsents,
                ),
            )
        }
    }

    private suspend fun completeConsentViewing() {
        // Skip if there is no standalone consent page in the queue
        if (_viewState.value.onboardingStatesQueue.none { it is ConsentPage }) return

        val consents = _viewState.value.consents
        val obtainedConsentId = consents.mapNotNull { consentModel ->
            consentModel.consent.id.takeIf { consentModel.isObtained }
        }

        controller.sendOnboardingEvent(
            eventType = AiutaAnalyticOnboardingEventType.CONSENT_GIVEN,
            pageId = AiutaAnalyticsPageId.CONSENT,
            consentsIds = obtainedConsentId,
        )

        consentInteractor::obtainConsent.safeInvoke(obtainedConsentId)
    }

    private fun solveIsPrimaryButtonEnabled(
        currentStep: OnboardingStep,
        consents: List<AiutaConsentUiModel>,
    ): Boolean {
        val isNotConsentPage = currentStep !is ConsentPage
        val isAllMandatoryChecked = consents.all { consentModel ->
            if (consentModel.consent.isRequired()) consentModel.isObtained else true
        }

        return isNotConsentPage || isAllMandatoryChecked
    }

    /**
     * Builds the page queue and initial consents from the SDK features. Lives in the view model so
     * the Screen stays a thin wiring layer — it only hands over the immutable [AiutaFeatures] and the
     * interactors, and the view model owns what the screen is made of.
     */
    private fun buildInitialState(): OnboardingScreenViewState {
        val onboardingFeature = features.strictProvideFeature<AiutaOnboardingFeature>()
        val consentFeature = features.provideFeature<AiutaConsentFeature>()
        val consentStandaloneOnboardingFeature =
            features.provideFeature<AiutaConsentStandaloneOnboardingPageFeature>()

        val onboardingStatesQueue = buildList {
            // Try on page
            add(TryOnPage(onboardingFeature.howItWorksPage))

            // Best result
            onboardingFeature.bestResultsPage?.let { bestResultsPageFeature ->
                add(BestResultPage(bestResultsPageFeature))
            }

            // Consent
            if (consentFeature is AiutaConsentStandaloneOnboardingPageFeature) {
                add(ConsentPage(consentFeature))
            }
        }

        val initialConsents = consentStandaloneOnboardingFeature?.toUiModels(
            obtainedConsentsIds = consentInteractor.obtainedConsentsIds.value,
        ).orEmpty()

        return OnboardingScreenViewState(
            onboardingStatesQueue = onboardingStatesQueue,
            currentStep = onboardingStatesQueue.first(),
            consents = initialConsents,
            isPrimaryButtonEnabled = solveIsPrimaryButtonEnabled(
                currentStep = onboardingStatesQueue.first(),
                consents = initialConsents,
            ),
        )
    }
}
