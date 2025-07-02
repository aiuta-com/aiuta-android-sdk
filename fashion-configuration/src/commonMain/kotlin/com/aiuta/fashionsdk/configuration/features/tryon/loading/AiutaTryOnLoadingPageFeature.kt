package com.aiuta.fashionsdk.configuration.features.tryon.loading

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.loading.strings.AiutaTryOnLoadingPageFeatureStrings
import com.aiuta.fashionsdk.configuration.features.tryon.loading.styles.AiutaTryOnLoadingPageFeatureStyles
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the try-on loading page feature.
 *
 * This feature manages the appearance and behavior of the loading screen
 * shown during try-on generation.
 *
 * Required components:
 * - [strings]: Text content for the loading interface
 * - [styles]: Visual styles for the loading interface
 *
 * @property strings Text content for the loading interface
 * @property styles Visual styles for the loading interface
 */
public class AiutaTryOnLoadingPageFeature(
    public val strings: AiutaTryOnLoadingPageFeatureStrings,
    public val styles: AiutaTryOnLoadingPageFeatureStyles,
) : AiutaFeature {

    /**
     * Builder for creating [AiutaTryOnLoadingPageFeature] instances.
     *
     * This builder ensures all required properties are set before creating the feature instance.
     */
    public class Builder : AiutaFeature.Builder {
        public var strings: AiutaTryOnLoadingPageFeatureStrings? = null
        public var styles: AiutaTryOnLoadingPageFeatureStyles? = null

        public override fun build(): AiutaTryOnLoadingPageFeature {
            val parentClass = "AiutaTryOnLoadingPageFeature"

            return AiutaTryOnLoadingPageFeature(
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
 * DSL function for configuring the loading page feature.
 *
 * This function allows for DSL-style configuration of the loading page feature
 * within the try-on feature configuration.
 *
 * ```kotlin
 * tryOn {
 *     loadingPage {
 *         strings = ...
 *         styles = ...
 *     }
 * }
 * ```
 *
 * @param block Configuration block for the loading page feature
 * @return The updated try-on feature builder
 */
public inline fun AiutaTryOnFeature.Builder.loadingPage(
    block: AiutaTryOnLoadingPageFeature.Builder.() -> Unit,
): AiutaTryOnFeature.Builder = apply {
    loadingPage = AiutaTryOnLoadingPageFeature.Builder().apply(block).build()
}
