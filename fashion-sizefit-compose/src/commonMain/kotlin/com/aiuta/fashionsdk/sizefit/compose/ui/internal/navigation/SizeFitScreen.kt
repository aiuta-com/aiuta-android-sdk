package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen

@Immutable
internal abstract class SizeFitScreen : AiutaNavigationScreen() {

    object Questionary : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_QUESTIONARY
    }

    object BellyShapeSettings : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_BELLY_SHAPE_SETTINGS
    }

    object BraSettings : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_BRA_SETTINGS
    }

    object RecommendationResult : SizeFitScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.SIZEFIT_RECOMMENDATION
    }
}
