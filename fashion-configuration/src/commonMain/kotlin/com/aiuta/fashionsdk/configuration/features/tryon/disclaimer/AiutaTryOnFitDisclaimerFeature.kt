package com.aiuta.fashionsdk.configuration.features.tryon.disclaimer

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.icons.AiutaTryOnFitDisclaimerFeatureIcons
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.strings.AiutaTryOnFitDisclaimerFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the fit disclaimer in the try-on feature.
 *
 * This feature manages the display of fit-related disclaimers and warnings
 * to users during the try-on experience.
 *
 * Required components:
 * - [icons]: Icon resources for disclaimer UI
 * - [strings]: Text content for disclaimer messages
 *
 * @property icons Icon resources for disclaimer UI elements
 * @property strings Text content for disclaimer messages
 */
public class AiutaTryOnFitDisclaimerFeature(
    public val icons: AiutaTryOnFitDisclaimerFeatureIcons,
    public val strings: AiutaTryOnFitDisclaimerFeatureStrings,
) : AiutaFeature {

    /**
     * Builder for creating [AiutaTryOnFitDisclaimerFeature] instances.
     */
    public class Builder : AiutaFeature.Builder {
        public var icons: AiutaTryOnFitDisclaimerFeatureIcons? = null
        public var strings: AiutaTryOnFitDisclaimerFeatureStrings? = null

        public override fun build(): AiutaTryOnFitDisclaimerFeature {
            val parentClass = "AiutaTryOnFitDisclaimerFeature"

            return AiutaTryOnFitDisclaimerFeature(
                icons = icons.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "icons",
                ),
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the fit disclaimer feature.
 *
 * ```kotlin
 * tryOn {
 *      fitDisclaimer {
 *          icons = ...
 *          strings = ...
 *      }
 * }
 * ```
 *
 * @param block Configuration block for the fit disclaimer feature
 * @return The updated [AiutaTryOnFeature.Builder]
 */
public inline fun AiutaTryOnFeature.Builder.fitDisclaimer(
    block: AiutaTryOnFitDisclaimerFeature.Builder.() -> Unit,
): AiutaTryOnFeature.Builder = apply {
    fitDisclaimer = AiutaTryOnFitDisclaimerFeature.Builder().apply(block).build()
}
