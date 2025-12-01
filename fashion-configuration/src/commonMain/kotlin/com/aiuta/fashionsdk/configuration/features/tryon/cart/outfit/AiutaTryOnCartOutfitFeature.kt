package com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.handler.AiutaTryOnCartOutfitFeatureHandler
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.strings.AiutaTryOnCartOutfitFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the outfit cart integration in the try-on feature.
 *
 * This feature manages the integration between outfit functionality and
 * the shopping cart, allowing users to add multiple products from an outfit
 * to the cart at once.
 *
 * Required components:
 * - [strings]: Text content configuration
 * - [handler]: Outfit cart event handling
 *
 * @property strings Text content for outfit cart-related UI elements
 * @property handler Event handler for outfit cart-related actions
 */
public class AiutaTryOnCartOutfitFeature(
    public val strings: AiutaTryOnCartOutfitFeatureStrings,
    public val handler: AiutaTryOnCartOutfitFeatureHandler,
) : AiutaFeature {

    /**
     * Builder for creating [AiutaTryOnCartOutfitFeature] instances.
     */
    public class Builder : AiutaFeature.Builder {
        public var strings: AiutaTryOnCartOutfitFeatureStrings? = null
        public var handler: AiutaTryOnCartOutfitFeatureHandler? = null

        public override fun build(): AiutaTryOnCartOutfitFeature {
            val parentClass = "AiutaTryOnCartOutfitFeature"

            return AiutaTryOnCartOutfitFeature(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
                handler = handler.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "handler",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the outfit feature.
 *
 * This function allows for DSL-style configuration of the outfit feature
 * within the cart feature configuration.
 *
 * ```kotlin
 * tryOn {
 *     cart {
 *         outfit {
 *             strings = ...
 *             handler = ...
 *         }
 *     }
 * }
 * ```
 *
 * @param block Configuration block for the outfit feature
 * @return The updated [AiutaTryOnCartFeature.Builder]
 */
public inline fun AiutaTryOnCartFeature.Builder.outfit(
    block: AiutaTryOnCartOutfitFeature.Builder.() -> Unit,
): AiutaTryOnCartFeature.Builder = apply {
    outfit = AiutaTryOnCartOutfitFeature.Builder().apply(block).build()
}
