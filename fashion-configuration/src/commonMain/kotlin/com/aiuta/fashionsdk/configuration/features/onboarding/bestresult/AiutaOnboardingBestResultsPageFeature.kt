package com.aiuta.fashionsdk.configuration.features.onboarding.bestresult

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.images.AiutaOnboardingBestResultsPageFeatureImages
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.strings.AiutaOnboardingBestResultsPageFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the "Best Results" page in the onboarding flow.
 *
 * This feature showcases shoesExample results to demonstrate the app's capabilities
 * and set user expectations. It includes sample images, icons, text strings,
 * and styling options.
 *
 * Required components:
 * - [images]: Sample images to showcase in the best results page
 * - [strings]: Text strings for the best results page
 */
public class AiutaOnboardingBestResultsPageFeature(
    public val images: AiutaOnboardingBestResultsPageFeatureImages,
    public val strings: AiutaOnboardingBestResultsPageFeatureStrings,
) : AiutaFeature {

    /**
     * Builder class for creating instances of [AiutaOnboardingBestResultsPageFeature].
     *
     * This builder ensures that all required components are provided before
     * creating the feature instance.
     */
    public class Builder : AiutaFeature.Builder {
        public var images: AiutaOnboardingBestResultsPageFeatureImages? = null
        public var strings: AiutaOnboardingBestResultsPageFeatureStrings? = null

        public override fun build(): AiutaOnboardingBestResultsPageFeature {
            val parentClass = "AiutaOnboardingBestResultsPage"

            return AiutaOnboardingBestResultsPageFeature(
                images = images.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "images",
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
 * DSL function for configuring the best results page in the onboarding flow.
 *
 * Example usage:
 * ```
 * onboarding {
 *     bestResultsPage {
 *         images = ...
 *         icons = ...
 *         strings = ...
 *         styles = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaOnboardingFeature.Builder.bestResultsPage(
    block: AiutaOnboardingBestResultsPageFeature.Builder.() -> Unit,
): AiutaOnboardingFeature.Builder = apply {
    bestResultsPage = AiutaOnboardingBestResultsPageFeature.Builder().apply(block).build()
}
