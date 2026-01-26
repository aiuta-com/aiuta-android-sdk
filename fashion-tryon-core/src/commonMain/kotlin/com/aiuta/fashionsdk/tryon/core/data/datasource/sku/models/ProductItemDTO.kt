package com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductItemDTO(
    @SerialName("category_google_id")
    val categoryGoogleId: String,
    @SerialName("category_google_name")
    val categoryGoogleName: String,
    @SerialName("color")
    val color: String,
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: String,
    @SerialName("image_urls")
    val imageUrls: List<String>,
    @SerialName("is_ready")
    val isReady: Boolean,
    @SerialName("sku_catalog_name")
    val skuCatalogName: String? = null,
    @SerialName("size_chart_code")
    val sizeChartCode: String? = null,
    @SerialName("sku_id")
    val skuId: String,
    @SerialName("title")
    val title: String,
)
