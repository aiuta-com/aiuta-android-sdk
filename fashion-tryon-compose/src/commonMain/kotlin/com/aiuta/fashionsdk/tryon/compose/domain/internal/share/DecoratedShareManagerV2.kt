package com.aiuta.fashionsdk.tryon.compose.domain.internal.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsShareEvent
import com.aiuta.fashionsdk.analytics.events.AiutaShareEventType
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.sendAnalyticEvent

internal class DecoratedShareManagerV2(
    private val actualShareManager: ShareManagerV2,
    private val controller: FashionTryOnController,
    private val logger: AiutaLogger?,
) : ShareManagerV2 {

    override suspend fun shareImages(
        content: String?,
        pageId: AiutaAnalyticsPageId,
        productIds: List<String>,
        imageUrls: List<String>,
        watermark: Painter?,
    ): Result<Unit> {
        // Decorate with analytic
        controller.sendAnalyticEvent(
            event = AiutaAnalyticsShareEvent(
                pageId = pageId,
                productIds = productIds,
                event = AiutaShareEventType.INITIATED,
                targetId = null,
            ),
        )
        logger?.d(
            message = buildString {
                appendLine("DecoratedShareManagerV2: Start share image")
                appendLine("pageId - $pageId")
                appendLine("productIds - $productIds")
                appendLine("imageUrls - $imageUrls")
                appendLine("is watermark passed - ${watermark != null}")
            },
        )

        // Call real implementation
        return actualShareManager.shareImages(
            content = content,
            pageId = pageId,
            productIds = productIds,
            imageUrls = imageUrls,
            watermark = watermark,
        ).onFailure { exception ->
            logger?.e("DecoratedShareManagerV2: Failed to share images", exception)
        }
    }
}

@Composable
internal fun rememberShareManagerV2(): ShareManagerV2 {
    val actualShareManager = rememberActualShareManagerV2()
    val controller = LocalController.current

    return remember(
        actualShareManager,
        controller,
    ) {
        DecoratedShareManagerV2(
            actualShareManager = actualShareManager,
            controller = controller,
            logger = controller.aiuta.logger,
        )
    }
}
