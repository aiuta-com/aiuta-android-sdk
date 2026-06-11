package com.aiuta.fashionsdk.configuration.features.onboarding

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.dataprovider.AiutaOnboardingFeatureDataProvider
import com.aiuta.fashionsdk.configuration.features.onboarding.howworks.AiutaOnboardingHowItWorksPageFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.strings.AiutaOnboardingFeatureStrings
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription

/**
 * Configuration for the onboarding feature in the fashion SDK.
 *
 * This feature provides a guided introduction to the app's functionality through
 * a series of pages explaining how the app works and showcasing best results.
 *
 * Required components:
 * - [howItWorksPage]: Configuration for the "How It Works" page
 * - [strings]: Text strings used throughout the onboarding flow
 * - [shapes]: Visual shapes and layouts for the onboarding UI
 * - [dataProvider]: Provider for onboarding data and content
 *
 * Optional components:
 * - [bestResultsPage]: Configuration for the "Best Results" showcase page
 */
@Immutable
public class AiutaOnboardingFeature(
    // Features
    public val howItWorksPage: AiutaOnboardingHowItWorksPageFeature,
    public val bestResultsPage: AiutaOnboardingBestResultsPageFeature?,
    // General
    public val strings: AiutaOnboardingFeatureStrings,
    public val dataProvider: AiutaOnboardingFeatureDataProvider,
) : AiutaFeature {

    /**
     * Builder class for creating instances of [AiutaOnboardingFeature].
     *
     * This builder ensures that all required components are provided before
     * creating the feature instance.
     */
    public class Builder : AiutaFeature.Builder {
        public var howItWorksPage: AiutaOnboardingHowItWorksPageFeature? = null
        public var bestResultsPage: AiutaOnboardingBestResultsPageFeature? = null
        public var strings: AiutaOnboardingFeatureStrings? = null
        public var dataProvider: AiutaOnboardingFeatureDataProvider? = null

        public override fun build(): AiutaOnboardingFeature {
            val parentClass = "AiutaOnboardingFeature"

            return AiutaOnboardingFeature(
                howItWorksPage = howItWorksPage.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "howItWorksPage",
                ),
                bestResultsPage = this.bestResultsPage,
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
                dataProvider = dataProvider.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "dataProvider",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the onboarding feature.
 *
 * Example usage:
 * ```
 * features {
 *     onboarding {
 *         howItWorksPage = howItWorksPage {
 *             // Configure how it works page
 *         }
 *         strings = ...
 *         shapes = ...
 *         dataProvider = ...
 *     }
 * }
 */
public inline fun AiutaFeatures.Builder.onboarding(
    block: AiutaOnboardingFeature.Builder.() -> Unit,
): AiutaFeatures.Builder = apply {
    onboarding = AiutaOnboardingFeature.Builder().apply(block).build()
}
