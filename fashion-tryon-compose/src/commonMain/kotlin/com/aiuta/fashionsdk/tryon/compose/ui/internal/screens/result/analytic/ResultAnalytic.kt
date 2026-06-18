package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEventType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.sendAnalyticEvent

internal fun FashionTryOnController.sendResultEvent(
    event: AiutaAnalyticsResultsEventType,
    pageId: AiutaAnalyticsPageId,
    productIds: List<String>,
) {
    sendAnalyticEvent(
        event = AiutaAnalyticsResultsEvent(
            event = event,
            productIds = productIds,
            pageId = pageId,
        ),
    )
}
