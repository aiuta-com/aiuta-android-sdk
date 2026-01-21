package com.aiuta.fashionsdk.configuration.features.sizefit.bra

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.bra.strings.AiutaSizeFitBraFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Bra size feature configuration for the Aiuta Size Fit functionality.
 *
 * This feature manages the bra size selection interface,
 * allowing users to specify their chest circumference and cup size
 * for more accurate size recommendations.
 *
 * Required components:
 * - [strings]: Text content and localization
 *
 * @property strings Required text content and localization configuration
 * @see AiutaFeature
 * @see AiutaSizeFitBraFeatureStrings
 */
@Immutable
public class AiutaSizeFitBraFeature(
    public val strings: AiutaSizeFitBraFeatureStrings,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaSizeFitBraFeature] instances.
     *
     * This builder ensures all required configuration is set before
     * creating the final bra feature configuration.
     */
    @AiutaDsl
    public class Builder : AiutaFeature.Builder {
        /**
         * Required text content and localization configuration.
         */
        public var strings: AiutaSizeFitBraFeatureStrings? = null

        /**
         * Creates an [AiutaSizeFitBraFeature] instance with the configured properties.
         *
         * @return Configured [AiutaSizeFitBraFeature] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public override fun build(): AiutaSizeFitBraFeature {
            val parentClass = "AiutaSizeFitBraFeature"

            return AiutaSizeFitBraFeature(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring bra size feature.
 *
 * This extension provides a convenient DSL for configuring the bra size feature
 * as part of the size fit feature setup.
 *
 * ```kotlin
 * sizeFit {
 *     bra {
 *         strings = AiutaSizeFitBraFeatureStrings.Default()
 *     }
 * }
 * ```
 *
 * @param block Configuration block for setting up the bra feature
 * @return The updated size fit feature builder
 */
public inline fun AiutaSizeFitFeature.Builder.bra(
    block: AiutaSizeFitBraFeature.Builder.() -> Unit,
): AiutaSizeFitFeature.Builder = apply {
    bra = AiutaSizeFitBraFeature.Builder().apply(block).build()
}
