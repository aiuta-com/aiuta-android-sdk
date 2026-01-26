package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.CentredTextBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.BestResultPage

@Composable
internal fun BestResultPageContent(
    modifier: Modifier = Modifier,
    state: BestResultPage,
) {
    val bestResultsPageFeature = strictProvideFeature<AiutaOnboardingBestResultsPageFeature>()

    sendPageEvent(pageId = AiutaAnalyticsPageId.BEST_RESULTS)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        BestImagesBlock(
            modifier =
            Modifier
                .weight(0.65f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            state = state,
        )

        CentredTextBlock(
            modifier =
            Modifier
                .weight(0.35f)
                .fillMaxWidth(),
            title = bestResultsPageFeature.strings.onboardingBestResultsTitle,
            subtitle = bestResultsPageFeature.strings.onboardingBestResultsDescription,
        )
    }
}

@Composable
private fun BestImagesBlock(
    modifier: Modifier = Modifier,
    state: BestResultPage,
) {
    val cardModifier = Modifier.fillMaxHeight().aspectRatio(0.55f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            state.goodImages.forEachIndexed { index, image ->
                BestResultPageItem(
                    modifier = cardModifier,
                    image = image,
                    isGoodImage = true,
                )

                if (index != state.goodImages.lastIndex) {
                    Spacer(Modifier.width(6.dp))
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            state.badImages.forEachIndexed { index, image ->
                BestResultPageItem(
                    modifier = cardModifier,
                    image = image,
                    isGoodImage = false,
                )

                if (index != state.goodImages.lastIndex) {
                    Spacer(Modifier.width(6.dp))
                }
            }
        }
    }
}
