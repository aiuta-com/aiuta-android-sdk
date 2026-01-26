package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation

internal abstract class SizeFitScreen : AiutaNavigationScreen() {

    @Immutable
    object Questionary : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_QUESTIONARY
    }

    @Immutable
    class RecommendationResult(
        val config: SizeFitConfigState,
        val recommendation: AiutaSizeFitRecommendation,
    ) : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_RECOMMENDATION
    }
}
