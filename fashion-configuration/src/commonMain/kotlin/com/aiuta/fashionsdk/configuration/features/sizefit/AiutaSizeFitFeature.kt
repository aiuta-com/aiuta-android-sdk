package com.aiuta.fashionsdk.configuration.features.sizefit

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.sizefit.icons.AiutaSizeFitFeatureIcons
import com.aiuta.fashionsdk.configuration.features.sizefit.strings.AiutaSizeFitFeatureStrings
import com.aiuta.fashionsdk.configuration.features.sizefit.styles.AiutaSizeFitFeatureStyles
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Size fit feature configuration for the Aiuta SDK.
 *
 * This feature manages the size recommendation experience, allowing users to
 * input their body measurements and preferences to receive personalized size
 * recommendations for clothing items.
 *
 * @property icons Required icon resources configuration
 * @property strings Required text content and localization configuration
 * @property styles Required visual styling configuration
 * @see AiutaFeature
 */
@Immutable
public class AiutaSizeFitFeature(
    // General
    public val icons: AiutaSizeFitFeatureIcons,
    public val strings: AiutaSizeFitFeatureStrings,
    public val styles: AiutaSizeFitFeatureStyles,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaSizeFitFeature] instances.
     *
     * This builder ensures all required components are configured before
     * creating the final size fit feature configuration.
     */
    @AiutaDsl
    public class Builder : AiutaFeature.Builder {
        /**
         * Required icon resources configuration.
         */
        public var icons: AiutaSizeFitFeatureIcons? = null

        /**
         * Required text content and localization configuration.
         */
        public var strings: AiutaSizeFitFeatureStrings? = null

        /**
         * Required visual styling configuration.
         */
        public var styles: AiutaSizeFitFeatureStyles? = null

        /**
         * Creates an [AiutaSizeFitFeature] instance with the configured properties.
         *
         * @return Configured [AiutaSizeFitFeature] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public override fun build(): AiutaSizeFitFeature {
            val parentClass = "AiutaSizeFitFeature"

            return AiutaSizeFitFeature(
                icons = icons.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "icons",
                ),
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
                styles = styles.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "styles",
                ),
            )
        }
    }
}

/**
 * Extension function for configuring size fit within an [AiutaFeatures.Builder].
 *
 * This extension provides a convenient DSL for configuring the size fit feature
 * as part of the main features setup.
 *
 * ```kotlin
 * val features = aiutaFeatures {
 *     sizeFit {
 *         // Required components
 *         icons = DefaultAiutaSizeFitFeatureIcons()
 *         strings = AiutaSizeFitFeatureStrings.Default()
 *         styles = AiutaSizeFitFeatureStyles.Default()
 *     }
 * }
 * ```
 *
 * @param block Configuration block for setting up the size fit feature
 * @return The features builder for method chaining
 * @see AiutaFeatures.Builder
 * @see AiutaSizeFitFeature.Builder
 */
public inline fun AiutaFeatures.Builder.sizeFit(
    block: AiutaSizeFitFeature.Builder.() -> Unit,
): AiutaFeatures.Builder = apply {
    sizeFit = AiutaSizeFitFeature.Builder().apply(block).build()
}
