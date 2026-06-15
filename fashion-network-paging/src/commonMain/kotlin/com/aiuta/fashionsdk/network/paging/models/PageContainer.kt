package com.aiuta.fashionsdk.network.paging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base container for paging response
 */
@Serializable
public data class PageContainer<T>(
    /**
     * Result list of new page
     */
    @SerialName("items")
    val items: List<T>,
    /**
     * Total number of items in list
     */
    @SerialName("total")
    var total: Int,
    /**
     * Offset for next page
     */
    @SerialName("next_offset")
    var nextOffset: Int? = null,
)
