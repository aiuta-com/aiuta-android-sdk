package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.FEEDBACK_EVENT)
public class AiutaAnalyticsFeedbackEvent(
    @SerialName("event")
    public val event: AiutaAnalyticsFeedbackEventType,
    @SerialName("negativeFeedbackOptionIndex")
    public val negativeFeedbackOptionIndex: Int? = null,
    @SerialName("negativeFeedbackText")
    public val negativeFeedbackText: String? = null,
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticsFeedbackEventType {
    @SerialName("positive")
    POSITIVE,

    @SerialName("negative")
    NEGATIVE,
}
