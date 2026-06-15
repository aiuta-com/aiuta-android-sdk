package com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductItemDTO(
    @SerialName("sku_id")
    val id: String,
    @SerialName("product_info")
    val productInfo: ProductInfo,

) {
    @Serializable
    internal data class ProductInfo(
        @SerialName("image_urls")
        val imageUrls: List<String>? = null,
        @SerialName("title")
        val title: String? = null,
    )
}
