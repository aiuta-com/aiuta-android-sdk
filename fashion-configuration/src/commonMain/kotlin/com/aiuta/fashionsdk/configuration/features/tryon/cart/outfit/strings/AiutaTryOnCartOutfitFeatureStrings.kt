package com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.strings

/**
 * Interface for outfit-related text strings in the cart feature.
 *
 * This interface defines the text strings used for outfit-related cart functionality,
 * allowing for localization and customization of user-facing text.
 *
 * @property addToCartOutfit Text displayed on the add outfit to cart button
 */
public interface AiutaTryOnCartOutfitFeatureStrings {
    public val addToCartOutfit: String

    /**
     * Default implementation of [AiutaTryOnCartOutfitFeatureStrings].
     */
    public class Default : AiutaTryOnCartOutfitFeatureStrings {
        override val addToCartOutfit: String = "Shop the look"
    }
}
