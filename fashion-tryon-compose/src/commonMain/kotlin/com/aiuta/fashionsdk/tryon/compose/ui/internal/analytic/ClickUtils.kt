package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsExitEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEventType
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.features.tryon.cart.handler.AiutaTryOnCartFeatureHandler
import com.aiuta.fashionsdk.configuration.features.wishlist.dataprovider.AiutaWishlistFeatureDataProvider
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendResultEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke

// Listeners
internal fun FashionTryOnController.clickAddToWishListActiveSKU(
    pageId: AiutaAnalyticsPageId,
    productId: String,
    updatedWishlistState: Boolean,
    dataProvider: AiutaWishlistFeatureDataProvider,
) {
    sendResultEvent(
        event = AiutaAnalyticsResultsEventType.PRODUCT_ADD_TO_WISHLIST,
        pageId = pageId,
        productId = productId,
    )
    dataProvider::setProductInWishlist.safeInvoke(productId, updatedWishlistState)
}

internal fun FashionTryOnController.clickAddToCart(
    pageId: AiutaAnalyticsPageId,
    productId: String,
    handler: AiutaTryOnCartFeatureHandler,
) {
    sendResultEvent(
        event = AiutaAnalyticsResultsEventType.PRODUCT_ADD_TO_CART,
        pageId = pageId,
        productId = productId,
    )
    handler::addToCart.safeInvoke(activeProductItem.value.id)
}

internal fun FashionTryOnController.clickClose(pageId: AiutaAnalyticsPageId? = null) {
    analytic.sendFinishSessionEvent(
        pageId = pageId ?: currentScreen.value.exitPageId,
        productItem = activeProductItem.value,
    )
    aiutaUserInterfaceActions::closeClick.safeInvoke()
}

// Senders
internal fun InternalAiutaAnalytic.sendFinishSessionEvent(
    pageId: AiutaAnalyticsPageId,
    productItem: ProductItem,
) {
    sendEvent(event = AiutaAnalyticsExitEvent(pageId = pageId, productIds = listOf(productItem.id)))
}
