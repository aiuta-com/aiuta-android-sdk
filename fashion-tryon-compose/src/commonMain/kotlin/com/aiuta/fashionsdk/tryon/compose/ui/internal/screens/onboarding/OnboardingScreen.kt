package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.molecules.button.FashionButton
import com.aiuta.fashionsdk.compose.molecules.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.molecules.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.leftToRightTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.rightToLeftTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best.BestResultPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.OnboardingAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent.ConsentPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent.SmallConsentContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon.TryOnPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.OnboardingController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.listenIsPrimaryButtonEnabled
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.nextPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.previousPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.rememberOnboardingController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.BestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.backhandler.BackHandler
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.onboarding.strictOnboardingFeature

@Composable
internal fun OnboardingScreen(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val configuration = LocalAiutaConfiguration.current
    val theme = LocalTheme.current

    val onboardingFeature = strictOnboardingFeature()
    val onboardingController = rememberOnboardingController()

    val generalHorizontalPadding = 16.dp

    BackHandler {
        onboardingController.previousPage(controller)
    }

    Column(
        modifier =
            modifier
                .background(theme.colors.background)
                .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingAppBar(
            modifier =
                Modifier
                    .padding(horizontal = generalHorizontalPadding)
                    .fillMaxWidth(),
            onboardingController = onboardingController,
        )

        Spacer(Modifier.height(32.dp))

        OnboardingScreenContent(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            onboardingController = onboardingController,
        )

        FashionButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = generalHorizontalPadding),
            text =
                with(onboardingController) {
                    val currentState = state.value
                    when {
                        currentState != onboardingStatesQueue.last() -> onboardingFeature.strings.onboardingButtonNext

                        currentState is TryOnPage && currentState.internalPages.getOrNull(
                            onboardingController.pagerState.settledPage,
                        ) != currentState.internalPages.last() -> onboardingFeature.strings.onboardingButtonNext

                        else -> onboardingFeature.strings.onboardingButtonStart
                    }
                },
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            isEnable = onboardingController.listenIsPrimaryButtonEnabled().value,
            onClick = {
                onboardingController.nextPage(
                    controller = controller,
                    configuration = configuration,
                )
            },
        )

        SmallConsentContent(
            modifier = Modifier.fillMaxWidth(),
            onboardingController = onboardingController,
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun OnboardingScreenContent(
    modifier: Modifier = Modifier,
    onboardingController: OnboardingController,
) {
    val onboardingState =
        updateTransition(
            targetState = onboardingController.state.value,
            label = "onboardingState",
        )

    Box(
        modifier = modifier,
    ) {
        // Just for collect pager state
        VerticalPager(
            modifier = Modifier.alpha(0f),
            state = onboardingController.pagerState,
        ) {}

        onboardingState.AnimatedContent(
            modifier = modifier,
            transitionSpec = {
                val initialIndex = onboardingController.onboardingStatesQueue.indexOf(initialState)
                val targetIndex = onboardingController.onboardingStatesQueue.indexOf(targetState)

                if (initialIndex < targetIndex) {
                    rightToLeftTransition
                } else {
                    leftToRightTransition
                }
            },
        ) { state ->
            when (state) {
                is TryOnPage -> {
                    TryOnPageContent(
                        modifier = Modifier.fillMaxSize(),
                        onboardingController = onboardingController,
                        state = state,
                    )
                }

                is BestResultPage -> {
                    BestResultPageContent(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                    )
                }

                is ConsentPage -> {
                    ConsentPageContent(
                        modifier = Modifier.fillMaxSize(),
                        onboardingController = onboardingController,
                    )
                }
            }
        }
    }
}
