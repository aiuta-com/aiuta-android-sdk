package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.preloadConfig
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationWithFirstOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.validateControllerCache
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateTo: (TryOnScreen) -> Unit,
) {
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dataController = LocalAiutaTryOnDataController.current

    val consentStandaloneFeature = provideFeature<AiutaConsentStandaloneOnboardingPageFeature>()

    val isOnboardingPassed = controller.onboardingInteractor.isOnboardingCompleted.collectAsState()

    LaunchedEffect(Unit) {
        // Try to preload config
        launch { dataController.preloadConfig() }

        // Validate controller
        validateControllerCache(aiuta = controller.aiuta)

        // Check operation history
        val countGeneratedOperation =
            controller.generatedOperationInteractor
                .countGeneratedOperation()
                .first()

        if (countGeneratedOperation > 0) {
            controller.updateActiveOperationWithFirstOrSetEmpty()
        }

        // Solve should show onboarding or not

        val shouldShowOnboarding = when {
            // Onboarding not provided
            !features.isFeatureInitialize<AiutaOnboardingFeature>() -> false
            // SDK didn't show onboarding
            !isOnboardingPassed.value -> true
            // If consent is part of onboarding and some mandatory consents not seen
            consentStandaloneFeature != null &&
                controller.consentInteractor.shouldShowConsent(
                    consentStandaloneFeature,
                ) -> true
            else -> false
        }

        if (shouldShowOnboarding) {
            val firstOnboardingScreen =
                when {
                    features.isFeatureInitialize<AiutaWelcomeScreenFeature>() -> TryOnScreen.Preonboarding
                    features.isFeatureInitialize<AiutaOnboardingFeature>() -> TryOnScreen.Onboarding
                    else -> TryOnScreen.ImageSelector
                }

            navigateTo(firstOnboardingScreen)
        } else {
            navigateTo(TryOnScreen.ImageSelector)
        }
    }

    Box(modifier = modifier)
}
