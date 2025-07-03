package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsHistoryEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsHistoryEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController

internal fun FashionTryOnController.sendDeleteHistoryEvent() {
    val productIds = selectorHolder
        .getList()
        .map { it.productIds }
        .flatten()
        .distinct()

    sendHistoryEvent(
        eventType = AiutaAnalyticsHistoryEventType.GENERATED_IMAGE_DELETED,
        productIds = productIds,
    )
}

internal fun FashionTryOnController.sendHistoryEvent(
    eventType: AiutaAnalyticsHistoryEventType,
    productIds: List<String>,
) {
    analytic.sendEvent(
        event = AiutaAnalyticsHistoryEvent(
            event = eventType,
            pageId = AiutaAnalyticsPageId.HISTORY,
            productIds = productIds,
        ),
    )
}
