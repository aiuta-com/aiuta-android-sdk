package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsOnboardingEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController

internal fun FashionTryOnController.sendOnboardingEvent(
    eventType: AiutaAnalyticOnboardingEventType,
    pageId: AiutaAnalyticsPageId,
    productId: String,
    consentsIds: List<String>?,
) {
    analytic.sendEvent(
        event = AiutaAnalyticsOnboardingEvent(
            event = eventType,
            pageId = pageId,
            productId = productId,
            consentsIds = consentsIds?.takeIf { it.isNotEmpty() },
        ),
    )
}
