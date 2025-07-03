package com.aiuta.fashionsdk.tryon.compose.domain.internal.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsShareEvent
import com.aiuta.fashionsdk.analytics.events.AiutaShareEventType
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAnalytic

internal class DecoratedShareManagerV2(
    private val actualShareManager: ShareManagerV2,
    private val analytic: InternalAiutaAnalytic,
) : ShareManagerV2 {

    override suspend fun shareImages(
        content: String?,
        pageId: AiutaAnalyticsPageId,
        productIds: List<String>,
        imageUrls: List<String>,
        watermark: Painter?,
    ): Result<Unit> {
        // Decorate with analytic
        analytic.sendEvent(
            event = AiutaAnalyticsShareEvent(
                pageId = pageId,
                productIds = productIds,
                event = AiutaShareEventType.INITIATED,
                targetId = null,
            ),
        )

        // Call real implementation
        return actualShareManager.shareImages(
            content = content,
            pageId = pageId,
            productIds = productIds,
            imageUrls = imageUrls,
            watermark = watermark,
        )
    }
}

@Composable
internal fun rememberShareManagerV2(): ShareManagerV2 {
    val actualShareManager = rememberActualShareManagerV2()
    val analytic = LocalAnalytic.current

    return remember(
        actualShareManager,
        analytic,
    ) {
        DecoratedShareManagerV2(
            actualShareManager = actualShareManager,
            analytic = analytic,
        )
    }
}
