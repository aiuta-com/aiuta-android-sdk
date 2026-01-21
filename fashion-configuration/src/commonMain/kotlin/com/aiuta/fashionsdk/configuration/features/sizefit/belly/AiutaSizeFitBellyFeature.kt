package com.aiuta.fashionsdk.configuration.features.sizefit.belly

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.belly.strings.AiutaSizeFitBellyFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Belly shape feature configuration for the Aiuta Size Fit functionality.
 *
 * This feature manages the belly and hips shape selection interface,
 * allowing users to specify their body shape characteristics for more
 * accurate size recommendations.
 *
 * Required components:
 * - [strings]: Text content and localization
 *
 * @property strings Required text content and localization configuration
 * @see AiutaFeature
 * @see AiutaSizeFitBellyFeatureStrings
 */
@Immutable
public class AiutaSizeFitBellyFeature(
    public val strings: AiutaSizeFitBellyFeatureStrings,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaSizeFitBellyFeature] instances.
     *
     * This builder ensures all required configuration is set before
     * creating the final belly feature configuration.
     */
    @AiutaDsl
    public class Builder : AiutaFeature.Builder {
        /**
         * Required text content and localization configuration.
         */
        public var strings: AiutaSizeFitBellyFeatureStrings? = null

        /**
         * Creates an [AiutaSizeFitBellyFeature] instance with the configured properties.
         *
         * @return Configured [AiutaSizeFitBellyFeature] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public override fun build(): AiutaSizeFitBellyFeature {
            val parentClass = "AiutaSizeFitBellyFeature"

            return AiutaSizeFitBellyFeature(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring belly shape feature.
 *
 * This extension provides a convenient DSL for configuring the belly shape feature
 * as part of the size fit feature setup.
 *
 * ```kotlin
 * sizeFit {
 *     belly {
 *         strings = AiutaSizeFitBellyFeatureStrings.Default()
 *     }
 * }
 * ```
 *
 * @param block Configuration block for setting up the belly feature
 * @return The updated size fit feature builder
 */
public inline fun AiutaSizeFitFeature.Builder.belly(
    block: AiutaSizeFitBellyFeature.Builder.() -> Unit,
): AiutaSizeFitFeature.Builder = apply {
    belly = AiutaSizeFitBellyFeature.Builder().apply(block).build()
}
