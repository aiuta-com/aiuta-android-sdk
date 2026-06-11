package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common.OnboardingMediaPageContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage

@Composable
internal fun TryOnPageContent(
    state: TryOnPage,
    modifier: Modifier = Modifier,
) {
    val tryOnPageFeature = strictProvideFeature<AiutaOnboardingHowItWorksPageFeature>()

    OnboardingMediaPageContent(
        modifier = modifier,
        media = state.mediaItem,
        title = tryOnPageFeature.strings.onboardingHowItWorksTitle,
        subtitle = tryOnPageFeature.strings.onboardingHowItWorksDescription,
        pageId = AiutaAnalyticsPageId.HOW_IT_WORKS,
    )
}
