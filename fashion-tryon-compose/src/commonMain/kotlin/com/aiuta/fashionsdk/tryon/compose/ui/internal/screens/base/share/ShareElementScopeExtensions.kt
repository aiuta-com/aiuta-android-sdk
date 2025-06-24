package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

internal fun ShareElementScope.onShare(
    activeProductItem: ProductItem,
    imageUrl: String?,
    pageId: AiutaAnalyticsPageId,
) {
    imageUrl?.let {
        onShare(
            activeProductItems = listOf(activeProductItem),
            imageUrls = listOf(imageUrl),
            pageId = pageId,
        )
    }
}
