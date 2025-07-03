package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.PAGE_EVENT)
public class AiutaAnalyticsPageEvent(
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticsPageId {
    @SerialName("welcome")
    WELCOME,

    @SerialName("howItWorks")
    HOW_IT_WORKS,

    @SerialName("bestResults")
    BEST_RESULTS,

    @SerialName("consent")
    CONSENT,

    @SerialName("imagePicker")
    IMAGE_PICKER,

    @SerialName("loading")
    LOADING,

    @SerialName("results")
    RESULTS,

    @SerialName("history")
    HISTORY,
}
