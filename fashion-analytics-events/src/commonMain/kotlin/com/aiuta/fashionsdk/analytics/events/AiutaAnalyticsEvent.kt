package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
public sealed interface AiutaAnalyticsEvent : InternalAiutaAnalyticsEvent {

    public fun serialize(): String = Json.encodeToString<InternalAiutaAnalyticsEvent>(this)

    public object EventType {
        public const val CONFIGURE_EVENT: String = "configure"
        public const val SESSION_EVENT: String = "session"
        public const val PAGE_EVENT: String = "page"
        public const val ONBOARDING_EVENT: String = "onboarding"
        public const val PICKER_EVENT: String = "picker"
        public const val TRY_ON_EVENT: String = "tryOn"
        public const val RESULTS_EVENT: String = "results"
        public const val FEEDBACK_EVENT: String = "feedback"
        public const val HISTORY_EVENT: String = "history"
        public const val SHARE_EVENT: String = "share"
        public const val EXIT_EVENT: String = "exit"
    }
}
