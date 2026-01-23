package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.CentredTextBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.OnboardingController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.changeInternalTryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage

@Composable
internal fun TryOnPageContent(
    modifier: Modifier = Modifier,
    onboardingController: OnboardingController,
    state: TryOnPage,
) {
    val tryOnPageFeature = strictProvideFeature<AiutaOnboardingHowItWorksPageFeature>()

    val currentPage =
        remember(onboardingController.pagerState.settledPage) {
            derivedStateOf {
                state.internalPages.getOrNull(onboardingController.pagerState.settledPage)
                    ?: state.internalPages.last()
            }
        }

    val currentPageTransition =
        updateTransition(
            targetState = currentPage.value,
            label = "currentPageTransition",
        )

    sendPageEvent(pageId = AiutaAnalyticsPageId.HOW_IT_WORKS)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        ImagesBlock(
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .weight(0.65f)
                .padding(horizontal = 20.dp),
            currentPageTransition = currentPageTransition,
            onboardingController = onboardingController,
            state = state,
        )

        CentredTextBlock(
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(0.35f),
            title = tryOnPageFeature.strings.onboardingHowItWorksTitle,
            subtitle = tryOnPageFeature.strings.onboardingHowItWorksDescription,
        )
    }
}

@Composable
private fun ImagesBlock(
    modifier: Modifier = Modifier,
    currentPageTransition: Transition<TryOnPage.InternalPage>,
    onboardingController: OnboardingController,
    state: TryOnPage,
) {
    val onboardingFeature = strictProvideFeature<AiutaOnboardingFeature>()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        currentPageTransition.AnimatedContent(
            modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { page ->
            AiutaImage(
                modifier =
                Modifier
                    .fillMaxSize()
                    .clip(onboardingFeature.shapes.onboardingImageLShape),
                image = page.mainImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier =
            Modifier
                .align(Alignment.CenterStart)
                .width(90.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            state.forEachIndexed { index, page ->
                key(page.uniqueGeneratedId) {
                    ItemContent(
                        itemImage = page.itemImage,
                        isActive = index == onboardingController.pagerState.settledPage,
                        onClick = {
                            onboardingController.changeInternalTryOnPage(index)
                        },
                    )
                }
            }
        }
    }
}
