package com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsResultsEventType
import com.aiuta.fashionsdk.configuration.features.tryon.cart.handler.AiutaTryOnCartFeatureHandler
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.handler.AiutaTryOnCartOutfitFeatureHandler
import com.aiuta.fashionsdk.configuration.features.wishlist.dataprovider.AiutaWishlistFeatureDataProvider
import com.aiuta.fashionsdk.internal.navigation.controller.AiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendResultEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke

// Listeners
internal fun FashionTryOnController.clickAddToWishListActiveSKU(
    pageId: AiutaAnalyticsPageId,
    productIds: List<String> = emptyList(),
    updatedWishlistState: Boolean,
    dataProvider: AiutaWishlistFeatureDataProvider,
) {
    sendResultEvent(
        event = AiutaAnalyticsResultsEventType.PRODUCT_ADD_TO_WISHLIST,
        pageId = pageId,
        productIds = productIds,
    )

    val providedProductIds = productIds
        .takeIf { it.isNotEmpty() }
        ?: activeProductItemsIds
    dataProvider::setProductInWishlist.safeInvoke(providedProductIds, updatedWishlistState)
}

internal fun FashionTryOnController.clickAddProductToCart(
    pageId: AiutaAnalyticsPageId,
    productId: String,
    handler: AiutaTryOnCartFeatureHandler,
) {
    sendResultEvent(
        event = AiutaAnalyticsResultsEventType.PRODUCT_ADD_TO_CART,
        pageId = pageId,
        productIds = listOf(productId),
    )
    handler::addToCart.safeInvoke(activeProductItemsIds.first())
}

internal fun FashionTryOnController.clickAddOutfitToCart(
    pageId: AiutaAnalyticsPageId,
    handler: AiutaTryOnCartOutfitFeatureHandler,
) {
    sendResultEvent(
        event = AiutaAnalyticsResultsEventType.PRODUCT_ADD_TO_CART,
        pageId = pageId,
        productIds = activeProductItemsIds,
    )
    handler::addToCartOutfit.safeInvoke(activeProductItemsIds)
}

internal fun FashionTryOnController.clickClose(
    navigationController: AiutaNavigationController,
    pageId: AiutaAnalyticsPageId? = null,
) {
    navigationController.clickClose(
        pageId = pageId,
        productIds = activeProductItemsIds,
    )
}
