package com.aiuta.fashionsdk.internal.subscription.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Subscription configuration served by the backend in `GET /subscription_details`.
 *
 * Only the fields the SDK uses are mapped; the backend can send more, they are
 * ignored. Every field is optional: a subscription may not configure it.
 *
 * @property poweredBySticker Settings of the "Powered by Aiuta" sticker
 * @property operationDelaysSequence Delays between try-on operation status polls
 */
@Serializable
public data class AiutaSubscriptionDetails(
    @SerialName("powered_by_sticker")
    public val poweredBySticker: AiutaPoweredBySticker? = null,
    @SerialName("operation_delays_sequence")
    public val operationDelaysSequence: List<AiutaOperationDelay>? = null,
)

/**
 * Settings of the "Powered by Aiuta" sticker.
 *
 * @property urlAndroid Link opened by the sticker on Android
 * @property urlIos Link opened by the sticker on iOS
 * @property isVisible Whether the sticker is shown
 */
@Serializable
public data class AiutaPoweredBySticker(
    @SerialName("url_android")
    public val urlAndroid: String? = null,
    @SerialName("url_ios")
    public val urlIos: String? = null,
    @SerialName("is_visible")
    public val isVisible: Boolean? = null,
)

/**
 * One step of the delays sequence between try-on operation status polls,
 * the same contract the iOS and Web SDKs use.
 *
 * @property mode `recurring` ([delay] waited [repeat] times) or `infinite`
 * ([delay] waited until the operation ends)
 * @property delay Seconds waited before each poll
 * @property repeat How many polls a `recurring` step makes
 */
@Serializable
public data class AiutaOperationDelay(
    @SerialName("mode")
    public val mode: String? = null,
    @SerialName("delay")
    public val delay: Double? = null,
    @SerialName("repeat")
    public val repeat: Int? = null,
)
