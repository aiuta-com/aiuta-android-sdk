package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.AiutaShoesModeOnboardingPage
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
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.ShoesBestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.OnboardingResolutionInput
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.navigateNextPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.navigatePreviousPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class OnboardingViewModel(
    private val features: AiutaFeatures,
    private val controller: FashionTryOnController,
    private val onboardingInteractor: OnboardingInteractor,
    private val consentInteractor: ConsentInteractor,
    private val mode: AiutaMode,
    private val onboardingShoesPage: AiutaShoesModeOnboardingPage?,
) : ViewModel() {

    private val onboardingFeature = features.strictProvideFeature<AiutaOnboardingFeature>()
    private val isGeneralBestResultsEnabled = onboardingFeature.bestResultsPage != null

    // Starts empty; the real queue is built once the per-mode completion state is loaded (see init).
    private val _viewState = MutableStateFlow(
        OnboardingScreenViewState(
            onboardingStatesQueue = emptyList(),
            currentStep = null,
            isPrimaryButtonEnabled = false,
        ),
    )
    val viewState = _viewState.asStateFlow()

    private val _viewAction = MutableStateFlow<OnboardingScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    init {
        viewModelScope.launch {
            // Splash already awaited completion before navigating here, so the first emission is
            // effectively immediate.
            val completion = onboardingInteractor.isOnboardingCompleted.first()
            val state = buildInitialState(completion)

            if (state.onboardingStatesQueue.isEmpty()) {
                _viewAction.update { OnboardingScreenAction.NavigateToImageSelector }
            } else {
                _viewState.value = state
            }
        }
    }

    fun obtainEvent(event: OnboardingScreenEvent) {
        when (event) {
            is OnboardingScreenEvent.BackClicked -> navigatePreviousPage()
            is OnboardingScreenEvent.CloseClicked -> {
                _viewAction.update { OnboardingScreenAction.Close(event.pageId) }
            }

            is OnboardingScreenEvent.NextClicked -> navigateNextPage()

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

    /**
     * Records onboarding completion triggered by pressing "Next" on [step]:
     * - "how it works": completes GENERAL only if the general best-results slide is disabled.
     * - general best-results: completes GENERAL.
     * - shoes best-results: completes SHOES.
     * - consent: nothing.
     */
    internal fun recordCompletionOnLeaving(step: OnboardingStep) {
        val modeToComplete: AiutaMode? = when (step) {
            is TryOnPage -> AiutaMode.GENERAL.takeIf { !isGeneralBestResultsEnabled }
            is BestResultPage -> AiutaMode.GENERAL
            is ShoesBestResultPage -> AiutaMode.SHOES
            is ConsentPage -> null
        }

        modeToComplete?.let { completedMode ->
            viewModelScope.launch {
                onboardingInteractor.completeOnboarding(completedMode)
            }
        }
    }

    /**
     * Finishes the onboarding flow once the queue is exhausted. Per-slide completion is handled by
     * [recordCompletionOnLeaving]; this only wraps up consent + analytics and navigates away.
     */
    internal fun finishOnboarding() {
        viewModelScope.launch {
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
        currentStep: OnboardingStep?,
        consents: List<AiutaConsentUiModel>,
    ): Boolean {
        val isNotConsentPage = currentStep !is ConsentPage
        val isAllMandatoryChecked = consents.all { consentModel ->
            if (consentModel.consent.isRequired()) consentModel.isObtained else true
        }

        return isNotConsentPage || isAllMandatoryChecked
    }

    /**
     * Builds the page queue and initial consents from the SDK features, the active [mode] and the
     * per-mode [completion] state, using the shared [OnboardingResolutionInput] rules so the queue
     * matches what the Splash flow decided to show. Lives in the view model so the Screen stays a
     * thin wiring layer.
     */
    private fun buildInitialState(completion: Map<AiutaMode, Boolean>): OnboardingScreenViewState {
        val consentFeature = features.provideFeature<AiutaConsentFeature>()
        val consentStandaloneOnboardingFeature =
            features.provideFeature<AiutaConsentStandaloneOnboardingPageFeature>()

        val resolution = OnboardingResolutionInput(
            mode = mode,
            isOnboardingEnabled = true,
            isHowItWorksEnabled = true,
            isGeneralBestResultsEnabled = isGeneralBestResultsEnabled,
            isShoesBestResultsEnabled = onboardingShoesPage != null,
            completion = completion,
        )

        val onboardingStatesQueue = buildList {
            // How it works
            if (resolution.showHowItWorks()) {
                add(TryOnPage(onboardingFeature.howItWorksPage))
            }

            // Best result (general)
            if (resolution.showGeneralBestResults()) {
                onboardingFeature.bestResultsPage?.let { bestResultsPageFeature ->
                    add(BestResultPage(bestResultsPageFeature))
                }
            }

            // Best result (shoes)
            if (resolution.showShoesBestResults()) {
                onboardingShoesPage?.let { shoesPage ->
                    add(ShoesBestResultPage(shoesPage))
                }
            }

            // Consent
            if (consentFeature is AiutaConsentStandaloneOnboardingPageFeature) {
                add(ConsentPage(consentFeature))
            }
        }

        val initialConsents = consentStandaloneOnboardingFeature?.toUiModels(
            obtainedConsentsIds = consentInteractor.obtainedConsentsIds.value,
        ).orEmpty()

        val currentStep = onboardingStatesQueue.firstOrNull()

        return OnboardingScreenViewState(
            onboardingStatesQueue = onboardingStatesQueue,
            currentStep = currentStep,
            consents = initialConsents,
            isPrimaryButtonEnabled = solveIsPrimaryButtonEnabled(
                currentStep = currentStep,
                consents = initialConsents,
            ),
        )
    }
}
