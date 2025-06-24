package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.ShareManagerV2
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.rememberShareManagerV2
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.painter.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
internal interface ShareElementScope {

    val shareFeature: AiutaShareFeature

    val isShareActive: State<Boolean>

    fun onShare(
        activeProductItems: List<ProductItem>,
        imageUrls: List<String>,
        pageId: AiutaAnalyticsPageId,
    )
}

@Composable
internal fun rememberShareElementScope(
    shareFeature: AiutaShareFeature,
): ShareElementScope {
    val scope = rememberCoroutineScope()
    val shareManager = rememberShareManagerV2()
    val watermarkPainter = shareFeature
        .watermark
        ?.images
        ?.logo
        ?.let { logo -> painterResource(logo) }

    return remember {
        ShareElementScopeInstance(
            shareFeature = shareFeature,
            shareManager = shareManager,
            watermarkPainter = watermarkPainter,
            scope = scope,
        )
    }
}

internal class ShareElementScopeInstance(
    override val shareFeature: AiutaShareFeature,
    private val shareManager: ShareManagerV2,
    private val watermarkPainter: Painter?,
    private val scope: CoroutineScope,
) : ShareElementScope {
    override val isShareActive = mutableStateOf(false)

    override fun onShare(
        activeProductItems: List<ProductItem>,
        imageUrls: List<String>,
        pageId: AiutaAnalyticsPageId,
    ) {
        if (activeProductItems.isEmpty()) return

        scope.launch {
            isShareActive.value = true

            val skuIds = activeProductItems.map { it.id }
            val shareText = shareFeature.dataProvider?.let { provider ->
                provider::getShareText.safeInvoke(skuIds)
            }

            shareManager.shareImages(
                content = shareText?.getOrNull(),
                pageId = pageId,
                productId = skuIds.first(),
                imageUrls = imageUrls,
                watermark = watermarkPainter,
            )

            isShareActive.value = false
        }
    }
}
