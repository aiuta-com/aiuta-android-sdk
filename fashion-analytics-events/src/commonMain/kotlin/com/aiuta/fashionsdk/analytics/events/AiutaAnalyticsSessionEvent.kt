package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.SESSION_EVENT)
public class AiutaAnalyticsSessionEvent(
    @SerialName("pageId")
    override val pageId: AiutaAnalyticsPageId? = null,
    @SerialName("productIds")
    override val productIds: List<String>,
    @SerialName("flow")
    public val flow: FlowType,
) : AiutaAnalyticsEvent {
    @Serializable
    public enum class FlowType {
        @SerialName("tryOn")
        TRY_ON,

        @SerialName("history")
        HISTORY,
    }
}
