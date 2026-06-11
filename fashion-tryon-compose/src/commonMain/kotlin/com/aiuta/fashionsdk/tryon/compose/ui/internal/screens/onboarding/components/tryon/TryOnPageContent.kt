package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaVideoSurface
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.CentredTextBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage

@Composable
internal fun TryOnPageContent(
    state: TryOnPage,
    modifier: Modifier = Modifier,
) {
    val tryOnPageFeature = strictProvideFeature<AiutaOnboardingHowItWorksPageFeature>()

    sendPageEvent(pageId = AiutaAnalyticsPageId.HOW_IT_WORKS)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.video?.let { media ->
            AiutaVideoSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f),
                video = media,
            )
        }

        CentredTextBlock(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f),
            title = tryOnPageFeature.strings.onboardingHowItWorksTitle,
            subtitle = tryOnPageFeature.strings.onboardingHowItWorksDescription,
        )
    }
}
