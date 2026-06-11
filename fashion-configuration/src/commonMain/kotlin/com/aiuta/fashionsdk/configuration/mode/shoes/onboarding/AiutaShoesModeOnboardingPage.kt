package com.aiuta.fashionsdk.configuration.mode.shoes.onboarding

import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.images.AiutaShoesModeOnboardingPageImages
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.strings.AiutaShoesModeOnboardingPageStrings

/**
 * Shoes-mode overrides for the onboarding "best results" page.
 *
 * Required components:
 * - [images]: Sample media shown on the best results page
 * - [strings]: Text strings for the best results page
 */
public class AiutaShoesModeOnboardingPage(
    public val images: AiutaShoesModeOnboardingPageImages,
    public val strings: AiutaShoesModeOnboardingPageStrings,
) {
    /**
     * Builder class for creating [AiutaShoesModeOnboardingPage] instances.
     *
     * This builder ensures that all required components are provided before
     * creating the page instance.
     */
    @AiutaDsl
    public class Builder {
        public var images: AiutaShoesModeOnboardingPageImages? = null
        public var strings: AiutaShoesModeOnboardingPageStrings? = null

        public fun build(): AiutaShoesModeOnboardingPage {
            val parentClass = "AiutaShoesModeOnboardingPage"

            return AiutaShoesModeOnboardingPage(
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
 * DSL function for configuring the shoes-mode onboarding best results page.
 *
 * Example usage:
 * ```
 * shoes {
 *     onboardingShoesPage {
 *         images = ...
 *         strings = ...
 *     }
 * }
 * ```
 */
public inline fun AiutaShoesMode.Builder.onboardingShoesPage(
    block: AiutaShoesModeOnboardingPage.Builder.() -> Unit,
): AiutaShoesMode.Builder = apply {
    onboardingShoesPage = AiutaShoesModeOnboardingPage.Builder().apply(block).build()
}
