package com.aiuta.fashionsdk.tryon.compose.domain.internal.share

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId

internal class JVMShareManagerV2 : ShareManagerV2 {
    override suspend fun shareImages(
        content: String?,
        pageId: AiutaAnalyticsPageId,
        productIds: List<String>,
        imageUrls: List<String>,
        watermark: Painter?,
    ): Result<Unit> {
        // TODO Add check on UI side
        // This platform not support share
        return Result.success(Unit)
    }
}

@Composable
internal actual fun rememberActualShareManagerV2(): ShareManagerV2 = JVMShareManagerV2()
