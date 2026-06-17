package com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductOperation(
    @SerialName("error")
    val error: String? = null,
    @SerialName("abort_reason")
    val abortReason: AbortReason? = null,
    @SerialName("generated_images")
    val generatedImages: List<GeneratedImage>,
    @SerialName("id")
    val id: String,
    @SerialName("status")
    val status: ProductOperationStatus,
)
