package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsFeedbackEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsFeedbackEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController

internal fun FashionTryOnController.sendGenerationFeedback(
    optionIndex: Int,
    feedback: String? = null,
    productIds: List<String>,
) {
    analytic.sendEvent(
        event = AiutaAnalyticsFeedbackEvent(
            event = AiutaAnalyticsFeedbackEventType.NEGATIVE,
            negativeFeedbackOptionIndex = optionIndex,
            negativeFeedbackText = feedback,
            pageId = AiutaAnalyticsPageId.RESULTS,
            productIds = productIds
                .takeIf { it.isNotEmpty() }
                ?: activeProductItemsIds,
        ),
    )
}

internal fun FashionTryOnController.sendLikeGenerationFeedback(
    sessionImage: SessionImageUIModel,
) {
    analytic.sendEvent(
        event = AiutaAnalyticsFeedbackEvent(
            event = AiutaAnalyticsFeedbackEventType.POSITIVE,
            pageId = AiutaAnalyticsPageId.RESULTS,
            productIds = sessionImage.productIds
                .takeIf { it.isNotEmpty() }
                ?: activeProductItemsIds,
        ),
    )
}
