package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnAbortedReasonType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnEventType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController

@Composable
internal fun sendSessionEvent(flow: AiutaAnalyticsSessionEvent.FlowType) {
    val controller = LocalController.current

    LaunchedEffect(Unit) {
        with(controller) {
            analytic.sendEvent(
                event = AiutaAnalyticsSessionEvent(
                    flow = flow,
                    productIds = activeProductItemsIds,
                ),
            )
        }
    }
}

internal fun FashionTryOnController.sendTerminateEvent() {
    analytic.sendEvent(
        event = AiutaAnalyticsTryOnEvent(
            event = AiutaAnalyticsTryOnEventType.TRY_ON_ABORTED,
            abortReason = AiutaAnalyticsTryOnAbortedReasonType.USER_CANCELED,
            pageId = AiutaAnalyticsPageId.LOADING,
            productIds = activeProductItemsIds,
        ),
    )
}
