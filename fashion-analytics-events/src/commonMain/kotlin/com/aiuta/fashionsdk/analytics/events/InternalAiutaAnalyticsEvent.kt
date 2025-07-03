package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
public sealed interface InternalAiutaAnalyticsEvent {
    public val pageId: AiutaAnalyticsPageId?

    public val productIds: List<String>
}
