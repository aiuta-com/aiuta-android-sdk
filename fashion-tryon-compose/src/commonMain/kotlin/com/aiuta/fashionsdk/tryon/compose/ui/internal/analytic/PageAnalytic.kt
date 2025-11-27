package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController

internal fun FashionTryOnController.sendPageEvent(pageId: AiutaAnalyticsPageId) {
    analytic.sendEvent(
        event = AiutaAnalyticsPageEvent(
            pageId = pageId,
            productIds = activeProductItemsIds,
        ),
    )
}

@Composable
internal fun sendPageEvent(pageId: AiutaAnalyticsPageId) {
    val controller = LocalController.current

    LaunchedEffect(Unit) {
        controller.sendPageEvent(pageId)
    }
}
