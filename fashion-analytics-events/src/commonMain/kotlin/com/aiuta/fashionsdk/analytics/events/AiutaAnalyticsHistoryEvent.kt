package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.HISTORY_EVENT)
public class AiutaAnalyticsHistoryEvent(
    @SerialName("event")
    public val event: AiutaAnalyticsHistoryEventType,
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticsHistoryEventType {
    @SerialName("generatedImageDeleted")
    GENERATED_IMAGE_DELETED,
}
