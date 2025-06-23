package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.screenPosition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.leftToRightTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.rightToLeftTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.ConsentScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.HistoryScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.ModelSelectorScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.OnboardingScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.preonboarding.PreOnboardingScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.GenerationResultScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.ImageSelectorScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh.SplashScreen

@Composable
internal fun NavigationContent(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val sharedModifier = Modifier.fillMaxSize()

    val transition =
        updateTransition(
            targetState = controller.currentScreen.value,
            label = "navigation transition",
        )

    transition.AnimatedContent(
        modifier = modifier,
        transitionSpec = {
            if (screenPosition(initialState) < screenPosition(targetState)) {
                rightToLeftTransition
            } else {
                leftToRightTransition
            }
        },
        contentKey = { it.id },
    ) { targetScreen ->
        when (targetScreen) {
            is NavigationScreen.Splash -> {
                SplashScreen(
                    modifier = sharedModifier,
                    navigateTo = controller::navigateTo,
                )
            }

            is NavigationScreen.Preonboarding -> {
                PreOnboardingScreen(
                    modifier = sharedModifier,
                )
            }

            is NavigationScreen.Onboarding -> {
                OnboardingScreen(
                    modifier = sharedModifier,
                )
            }

            is NavigationScreen.History -> {
                HistoryScreen(
                    modifier = sharedModifier,
                )
            }

            is NavigationScreen.ImageSelector -> {
                ImageSelectorScreen(
                    modifier = sharedModifier,
                )
            }

            is NavigationScreen.Consent -> {
                ConsentScreen(
                    modifier = sharedModifier,
                    onObtainedConsents = targetScreen.onObtainedConsents,
                )
            }

            is NavigationScreen.ModelSelector -> {
                ModelSelectorScreen(
                    modifier = sharedModifier,
                )
            }

            is NavigationScreen.GenerationResult -> {
                GenerationResultScreen(
                    modifier = sharedModifier,
                )
            }
        }
    }
}
