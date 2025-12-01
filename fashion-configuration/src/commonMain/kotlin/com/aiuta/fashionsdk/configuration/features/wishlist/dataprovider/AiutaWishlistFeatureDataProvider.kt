package com.aiuta.fashionsdk.configuration.features.wishlist.dataprovider

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining the data provider for the wishlist feature.
 *
 * This interface provides functionality to manage wishlist items,
 * including observing the current wishlist state and modifying it.
 */
public interface AiutaWishlistFeatureDataProvider {
    /**
     * A [StateFlow] containing the list of product IDs currently in the wishlist.
     * This flow can be observed to react to changes in the wishlist state.
     */
    public val wishlistProductIds: StateFlow<List<String>>

    /**
     * Updates the wishlist status of a product.
     *
     * @param productIds The ID of the products to update
     * @param inWishlist Whether the product should be in the wishlist (true) or not (false)
     */
    public fun setProductInWishlist(productIds: List<String>, inWishlist: Boolean)
}
