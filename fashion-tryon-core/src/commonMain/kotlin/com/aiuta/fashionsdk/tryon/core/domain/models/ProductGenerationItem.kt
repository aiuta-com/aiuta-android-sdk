package com.aiuta.fashionsdk.tryon.core.domain.models

import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models.ProductItemDTO

/**
 * One of possible item for generation product flow
 *
 * @param productId - Id of product to generate
 * @param catalogName - Name of catalog with current [productId]
 */
public data class ProductGenerationItem(
    val productId: String,
    val catalogName: String? = null,
    val imageUrls: List<String>,
    val title: String,
    val isReady: Boolean,
    val sizeChartCode: String? = null,
)

internal fun ProductItemDTO.toPublic(): ProductGenerationItem = ProductGenerationItem(
    productId = skuId,
    catalogName = skuCatalogName,
    imageUrls = imageUrls,
    title = title,
    isReady = isReady,
    sizeChartCode = sizeChartCode,
)
