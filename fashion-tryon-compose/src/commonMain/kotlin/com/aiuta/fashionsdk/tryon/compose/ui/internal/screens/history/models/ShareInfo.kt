package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

internal class ShareInfo(
    val activeProductItems: List<ProductItem>,
    val imageUrls: List<String>,
    val pageId: AiutaAnalyticsPageId,
)
