package com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.handler

/**
 * Interface for handling outfit-related cart actions in the try-on feature.
 *
 * This interface defines the contract for implementing outfit cart functionality,
 * allowing integration with external shopping cart systems for adding multiple products
 * from an outfit to the cart.
 *
 * @property addToCartOutfit Adds multiple products from an outfit to the shopping cart
 * @param productIds List of product identifiers to add to cart
 */
public interface AiutaTryOnCartOutfitFeatureHandler {

    public fun addToCartOutfit(productIds: List<String>)
}
