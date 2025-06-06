package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsHistoryEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsHistoryEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController

internal fun FashionTryOnController.sendHistoryEvent(eventType: AiutaAnalyticsHistoryEventType) {
    analytic.sendEvent(
        event = AiutaAnalyticsHistoryEvent(
            event = eventType,
            pageId = AiutaAnalyticsPageId.HISTORY,
            productId = activeProductItem.value.id,
        ),
    )
}
