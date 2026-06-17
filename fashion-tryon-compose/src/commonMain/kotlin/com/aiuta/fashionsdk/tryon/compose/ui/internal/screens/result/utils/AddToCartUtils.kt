package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.utils

import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.AiutaTryOnCartOutfitFeature

/**
 * Resolves the add-to-cart button label for the unified result layout:
 * - single product -> [AiutaTryOnCartFeature] "add to cart"
 * - several products (outfit) -> [AiutaTryOnCartOutfitFeature] "add outfit to cart", falling back to
 *   the single-product label when the outfit feature is not configured.
 */
internal fun AiutaFeatures.resolveAddToCartText(isSingleMode: Boolean): String {
    val cartText = provideFeature<AiutaTryOnCartFeature>()?.strings?.addToCart.orEmpty()
    if (isSingleMode) return cartText

    val outfitText = provideFeature<AiutaTryOnCartOutfitFeature>()?.strings?.addToCartOutfit
    return outfitText ?: cartText
}
