package com.aiuta.fashionsdk.tryon.core.domain.models

import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models.ProductItemDTO

/**
 * One of possible item for generation product flow
 *
 * @param productId - Id of product to generate
 */
public data class ProductGenerationItem(
    val productId: String,
    val imageUrls: List<String>,
    val title: String,
)

internal fun ProductItemDTO.toPublic(): ProductGenerationItem = ProductGenerationItem(
    productId = id,
    imageUrls = productInfo.imageUrls.orEmpty(),
    title = productInfo.title.orEmpty(),
)
