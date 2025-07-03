package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.RESULTS_EVENT)
public class AiutaAnalyticsResultsEvent(
    @SerialName("event")
    public val event: AiutaAnalyticsResultsEventType,
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticsResultsEventType {
    @SerialName("productAddToWishlist")
    PRODUCT_ADD_TO_WISHLIST,

    @SerialName("productAddToCart")
    PRODUCT_ADD_TO_CART,

    @SerialName("pickOtherPhoto")
    PICK_OTHER_PHOTO,
}
