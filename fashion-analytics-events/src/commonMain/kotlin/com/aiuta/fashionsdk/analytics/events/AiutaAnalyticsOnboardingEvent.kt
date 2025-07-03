package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.ONBOARDING_EVENT)
public class AiutaAnalyticsOnboardingEvent(
    @SerialName("event")
    public val event: AiutaAnalyticOnboardingEventType,
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
    @SerialName("consentsIds")
    public val consentsIds: List<String>? = null,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticOnboardingEventType {
    @SerialName("welcomeStartClicked")
    WELCOME_START_CLICKED,

    @SerialName("consentGiven")
    CONSENT_GIVEN,

    @SerialName("onboardingFinished")
    ONBOARDING_FINISHED,
}
