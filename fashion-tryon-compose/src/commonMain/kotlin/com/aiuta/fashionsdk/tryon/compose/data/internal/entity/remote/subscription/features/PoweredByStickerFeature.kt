package com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PoweredByStickerFeature(
    @SerialName("url_android")
    val urlAndroid: String? = null,
    @SerialName("url_ios")
    val urlIos: String? = null,
    @SerialName("is_visible")
    val isVisible: Boolean? = null,
)
