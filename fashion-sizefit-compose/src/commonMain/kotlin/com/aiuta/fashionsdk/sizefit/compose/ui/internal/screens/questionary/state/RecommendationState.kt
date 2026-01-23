package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state

import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation

internal sealed interface RecommendationState {

    object Idle : RecommendationState

    object Loading : RecommendationState

    class Success(
        val recommendation: AiutaSizeFitRecommendation,
    ) : RecommendationState

    object Error : RecommendationState
}
