package com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Part of the subscription details the try-on core needs: delays between
 * operation status polls. Same contract as the iOS and Web SDKs.
 */
@Serializable
internal data class OperationDelaysDetails(
    @SerialName("operation_delays_sequence")
    val operationDelaysSequence: List<OperationDelay>? = null,
)

/**
 * One step of the sequence: [delay] seconds waited before each poll, either
 * [repeat] times ("recurring") or until the operation ends ("infinite")
 */
@Serializable
internal data class OperationDelay(
    @SerialName("mode")
    val mode: String? = null,
    @SerialName("delay")
    val delay: Double? = null,
    @SerialName("repeat")
    val repeat: Int? = null,
)
