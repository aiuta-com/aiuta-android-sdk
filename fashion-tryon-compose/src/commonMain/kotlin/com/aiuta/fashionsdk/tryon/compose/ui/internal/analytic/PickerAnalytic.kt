package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.sendAnalyticEvent

internal fun FashionTryOnController.sendPickerAnalytic(
    event: AiutaAnalyticsPickerEventType,
    pageId: AiutaAnalyticsPageId,
) {
    sendAnalyticEvent(
        event = AiutaAnalyticsPickerEvent(
            event = event,
            pageId = pageId,
            productIds = activeProductItemsIds,
        ),
    )
}

@Composable
internal fun sendPickerAnalytic(
    event: AiutaAnalyticsPickerEventType,
    pageId: AiutaAnalyticsPageId,
) {
    val controller = LocalController.current

    LaunchedEffect(Unit) {
        controller.sendPickerAnalytic(
            event = event,
            pageId = pageId,
        )
    }
}
