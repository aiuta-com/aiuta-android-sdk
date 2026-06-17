package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature
import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.AiutaShoesModeOnboardingPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.AiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.preloadConfig
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationWithFirstOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.validateControllerCache
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.OnboardingResolutionInput
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh.models.SplashScreenAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SplashViewModel(
    private val features: AiutaFeatures,
    private val controller: FashionTryOnController,
    private val dataController: AiutaTryOnDataController,
    private val mode: AiutaMode,
    private val onboardingShoesPage: AiutaShoesModeOnboardingPage?,
) : ViewModel() {

    private val _viewAction = MutableStateFlow<SplashScreenAction?>(null)
    val viewAction = _viewAction.asStateFlow()

    init {
        viewModelScope.launch {
            // Try to preload config
            launch { dataController.preloadConfig() }

            // Validate controller
            validateControllerCache(aiuta = controller.aiuta)

            // Check operation history
            val countGeneratedOperation = controller.generatedOperationInteractor
                .countGeneratedOperation()
                .first()
            if (countGeneratedOperation > 0) {
                controller.updateActiveOperationWithFirstOrSetEmpty()
            }

            _viewAction.update { SplashScreenAction.NavigateTo(resolveNextScreen()) }
        }
    }

    fun clearAction() {
        _viewAction.value = null
    }

    private suspend fun resolveNextScreen(): TryOnScreen {
        val onboardingFeature = features.provideFeature<AiutaOnboardingFeature>()
        val consentStandaloneFeature = features.provideFeature<AiutaConsentStandaloneOnboardingPageFeature>()

        val completion = controller.onboardingInteractor.isOnboardingCompleted.first()

        val resolution = OnboardingResolutionInput(
            mode = mode,
            isOnboardingEnabled = onboardingFeature != null,
            isHowItWorksEnabled = onboardingFeature?.howItWorksPage != null,
            isGeneralBestResultsEnabled = onboardingFeature?.bestResultsPage != null,
            isShoesBestResultsEnabled = onboardingShoesPage != null,
            completion = completion,
        )

        // If consent is part of onboarding and some mandatory consents have not been seen yet.
        val consentNeedsShow = consentStandaloneFeature != null &&
            controller.consentInteractor.shouldShowConsent(consentStandaloneFeature)

        val shouldShowOnboarding = resolution.anySlideToShow() || consentNeedsShow

        return when {
            !shouldShowOnboarding -> TryOnScreen.ImageSelector
            features.isFeatureInitialize<AiutaWelcomeScreenFeature>() -> TryOnScreen.Preonboarding
            features.isFeatureInitialize<AiutaOnboardingFeature>() -> TryOnScreen.Onboarding
            else -> TryOnScreen.ImageSelector
        }
    }
}
