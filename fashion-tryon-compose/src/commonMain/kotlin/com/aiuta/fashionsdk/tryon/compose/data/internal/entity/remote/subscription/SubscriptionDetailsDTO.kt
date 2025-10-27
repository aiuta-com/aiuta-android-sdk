package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription

import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SubscriptionDetailsDTO(
    @SerialName("powered_by_sticker")
    val poweredByStickerFeature: PoweredByStickerFeature? = null,
)
