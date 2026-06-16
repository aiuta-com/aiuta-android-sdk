package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.OnboardingMediaPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.ShoesBestResultPage

@Composable
internal fun ShoesBestResultPageContent(
    modifier: Modifier = Modifier,
    state: ShoesBestResultPage,
) {
    OnboardingMediaPageContent(
        modifier = modifier,
        media = state.mediaItem,
        title = state.title,
        subtitle = state.subtitle,
        pageId = AiutaAnalyticsPageId.BEST_RESULTS,
    )
}
