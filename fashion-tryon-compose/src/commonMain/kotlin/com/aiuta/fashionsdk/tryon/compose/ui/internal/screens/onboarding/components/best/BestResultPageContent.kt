package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.OnboardingMediaPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.BestResultPage

@Composable
internal fun BestResultPageContent(
    modifier: Modifier = Modifier,
    state: BestResultPage,
) {
    val bestResultsPageFeature = strictProvideFeature<AiutaOnboardingBestResultsPageFeature>()

    OnboardingMediaPageContent(
        modifier = modifier,
        media = state.mediaItem,
        title = bestResultsPageFeature.strings.onboardingBestResultsTitle,
        subtitle = bestResultsPageFeature.strings.onboardingBestResultsDescription,
        pageId = AiutaAnalyticsPageId.BEST_RESULTS,
    )
}
