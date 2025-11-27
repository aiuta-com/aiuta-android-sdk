package com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateProductOperationRequest(
    @SerialName("sku_ids")
    val skuIds: List<String>,
    @SerialName("uploaded_image_id")
    val uploadedImageId: String,
)
