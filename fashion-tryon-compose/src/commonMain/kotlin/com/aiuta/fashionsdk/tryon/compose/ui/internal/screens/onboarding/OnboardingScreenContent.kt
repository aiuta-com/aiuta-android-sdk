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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneOnboardingPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.leftToRightTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.transition.rightToLeftTransition
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.ConsentContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best.BestResultPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.OnboardingAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent.SmallConsentContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon.TryOnPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.BestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenViewState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.utils.solvePrimaryButtonText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun OnboardingScreenContent(
    viewState: State<OnboardingScreenViewState>,
    pagerState: PagerState,
    eventHandler: (OnboardingScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val onboardingFeature = strictProvideFeature<AiutaOnboardingFeature>()
    val consentStandaloneOnboardingFeature = provideFeature<AiutaConsentStandaloneOnboardingPageFeature>()

    val generalHorizontalPadding = 16.dp

    BackHandler {
        eventHandler(OnboardingScreenEvent.BackClicked(settledPage = pagerState.settledPage))
    }

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingAppBar(
            modifier = Modifier
                .padding(horizontal = generalHorizontalPadding)
                .fillMaxWidth(),
            viewState = viewState,
            pagerState = pagerState,
            eventHandler = eventHandler,
        )

        Spacer(Modifier.height(32.dp))

        OnboardingPages(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            viewState = viewState,
            pagerState = pagerState,
            eventHandler = eventHandler,
        )

        FashionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = generalHorizontalPadding),
            text = solvePrimaryButtonText(
                viewState = viewState,
                pagerState = pagerState,
                onboardingFeature = onboardingFeature,
                consentStandaloneOnboardingFeature = consentStandaloneOnboardingFeature,
            ),
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            isEnable = viewState.value.isPrimaryButtonEnabled,
            onClick = {
                eventHandler(OnboardingScreenEvent.NextClicked(settledPage = pagerState.settledPage))
            },
        )

        SmallConsentContent(
            modifier = Modifier.fillMaxWidth(),
            viewState = viewState,
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun OnboardingPages(
    viewState: State<OnboardingScreenViewState>,
    pagerState: PagerState,
    eventHandler: (OnboardingScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewState.value
    val onboardingStatesQueue = state.onboardingStatesQueue

    val onboardingState =
        updateTransition(
            targetState = state.currentStep,
            label = "onboardingState",
        )

    Box(
        modifier = modifier,
    ) {
        // Just for collect pager state
        VerticalPager(
            modifier = Modifier.alpha(0f),
            state = pagerState,
        ) {}

        onboardingState.AnimatedContent(
            modifier = modifier,
            transitionSpec = {
                val initialIndex = onboardingStatesQueue.indexOf(initialState)
                val targetIndex = onboardingStatesQueue.indexOf(targetState)

                if (initialIndex < targetIndex) {
                    rightToLeftTransition
                } else {
                    leftToRightTransition
                }
            },
        ) { step ->
            when (step) {
                is TryOnPage -> {
                    TryOnPageContent(
                        modifier = Modifier.fillMaxSize(),
                        pagerState = pagerState,
                        state = step,
                        eventHandler = eventHandler,
                    )
                }

                is BestResultPage -> {
                    BestResultPageContent(
                        modifier = Modifier.fillMaxSize(),
                        state = step,
                    )
                }

                is ConsentPage -> {
                    ConsentContent(
                        modifier = Modifier.fillMaxSize(),
                        consentsList = state.consents,
                        onUpdateConsentState = { consent, isObtained ->
                            eventHandler(
                                OnboardingScreenEvent.ConsentToggled(
                                    consent = consent,
                                    isObtained = isObtained,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}
