package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.compose.tokens.images.AiutaImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnStringResources
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.BestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.OnboardingState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun rememberOnboardingController(): OnboardingController {
    val theme = LocalTheme.current
    val stringResources = LocalAiutaTryOnStringResources.current

    // Try on pages + Best result page
    val pagerState =
        rememberPagerState(
            pageCount = {
                TryOnPage(theme.images).internalPages.size + 2
            },
        )
    val scope = rememberCoroutineScope()

    return remember {
        OnboardingController(
            aiutaImages = theme.images,
            supplementPoint = stringResources.onboardingPageConsentSupplementaryPoints,
            pagerState = pagerState,
            scope = scope,
        )
    }
}

@Immutable
internal class OnboardingController(
    aiutaImages: AiutaImages,
    supplementPoint: List<String>,
    val pagerState: PagerState,
    internal val scope: CoroutineScope,
) {
    // Agreement
    val isMandatoryAgreementChecked = mutableStateOf(false)
    val supplementPointsMap =
        mutableStateMapOf<String, Boolean>(
            *supplementPoint.map {
                it to false
            }.toTypedArray(),
        )

    // Onboarding queue
    val onboardingStatesQueue =
        listOf(
            TryOnPage(aiutaImages),
            BestResultPage(aiutaImages),
            ConsentPage,
        )

    // General state
    val state: MutableState<OnboardingState> = mutableStateOf(onboardingStatesQueue.first())
}
