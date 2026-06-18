package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AiutaAnalyticsMode {
    @SerialName("general")
    GENERAL,

    @SerialName("shoes")
    SHOES,
}
