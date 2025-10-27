package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription

import androidx.compose.runtime.Immutable

@Immutable
internal data class SubscriptionDetailsConfig(
    val etag: String? = null,
    val details: SubscriptionDetailsDTO? = null,
)
