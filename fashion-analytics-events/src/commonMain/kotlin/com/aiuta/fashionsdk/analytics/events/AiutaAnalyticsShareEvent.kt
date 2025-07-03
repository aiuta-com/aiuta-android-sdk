package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.SHARE_EVENT)
public class AiutaAnalyticsShareEvent(
    @SerialName("pageId")
    override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
    @SerialName("event")
    public val event: AiutaShareEventType,
    @SerialName("targetId")
    public val targetId: String?,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaShareEventType {
    @SerialName("initiated")
    INITIATED,

    @SerialName("succeeded")
    SUCCEEDED,
}
