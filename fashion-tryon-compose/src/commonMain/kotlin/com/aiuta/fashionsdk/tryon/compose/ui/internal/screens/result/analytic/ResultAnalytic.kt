package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEventType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController

internal fun FashionTryOnController.sendResultEvent(
    event: AiutaAnalyticsResultsEventType,
    pageId: AiutaAnalyticsPageId,
    productIds: List<String>,
) {
    analytic.sendEvent(
        event = AiutaAnalyticsResultsEvent(
            event = event,
            productIds = productIds,
            pageId = pageId,
        ),
    )
}
