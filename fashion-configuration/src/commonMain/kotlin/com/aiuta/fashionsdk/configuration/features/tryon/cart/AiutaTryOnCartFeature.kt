package com.aiuta.fashionsdk.configuration.features.tryon.cart

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.handler.AiutaTryOnCartFeatureHandler
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.AiutaTryOnCartOutfitFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.strings.AiutaTryOnCartFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the shopping cart integration in the try-on feature.
 *
 * This feature manages the integration between the try-on experience and
 * the shopping cart functionality, handling product selection and purchase flow.
 *
 * Required components:
 * - [strings]: Text content configuration
 * - [handler]: Cart event handling
 *
 * Optional components:
 * - [outfit]: Outfit cart integration configuration
 *
 * @property strings Text content for cart-related UI elements
 * @property handler Event handler for cart-related actions
 * @property outfit Optional configuration for outfit cart functionality
 */
public class AiutaTryOnCartFeature(
    // Features
    public val outfit: AiutaTryOnCartOutfitFeature?,
    // General
    public val strings: AiutaTryOnCartFeatureStrings,
    public val handler: AiutaTryOnCartFeatureHandler,
) : AiutaFeature {

    /**
     * Builder for creating [AiutaTryOnCartFeature] instances.
     */
    public class Builder : AiutaFeature.Builder {
        public var outfit: AiutaTryOnCartOutfitFeature? = null
        public var strings: AiutaTryOnCartFeatureStrings? = null
        public var handler: AiutaTryOnCartFeatureHandler? = null

        public override fun build(): AiutaTryOnCartFeature {
            val parentClass = "AiutaTryOnCartFeature"

            return AiutaTryOnCartFeature(
                outfit = outfit,
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
 * DSL function for configuring the cart feature.
 *
 * This function allows for DSL-style configuration of the cart feature
 * within the try-on feature configuration.
 *
 * ```kotlin
 * tryOn {
 *     cart {
 *         strings = ...
 *         handler = ...
 *         // Optional outfit configuration
 *         outfit {
 *             strings = ...
 *             handler = ...
 *         }
 *     }
 * }
 * ```
 *
 * @param block Configuration block for the cart feature
 * @return The updated [AiutaTryOnFeature.Builder]
 */
public inline fun AiutaTryOnFeature.Builder.cart(
    block: AiutaTryOnCartFeature.Builder.() -> Unit,
): AiutaTryOnFeature.Builder = apply {
    cart = AiutaTryOnCartFeature.Builder().apply(block).build()
}
