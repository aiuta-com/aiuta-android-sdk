package com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateProductOperationResponse(
    @SerialName("details")
    val details: String? = null,
    @SerialName("errors")
    val errors: List<String>?,
    @SerialName("operation_id")
    val operationId: String,
)
