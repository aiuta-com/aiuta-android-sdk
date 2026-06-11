package com.aiuta.fashionsdk.configuration.mode.shoes

import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.mode.AiutaModes
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.AiutaShoesModeOnboardingPage
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.AiutaShoesModeImagePicker

/**
 * Configuration overrides applied when the SDK runs in shoes try-on mode.
 *
 * Required components:
 * - [onboardingShoesPage]: Overrides for the onboarding "best results" page
 * - [imagePicker]: Overrides for the image picker surfaces
 */
public class AiutaShoesMode(
    public val onboardingShoesPage: AiutaShoesModeOnboardingPage,
    public val imagePicker: AiutaShoesModeImagePicker,
) {
    /**
     * Builder class for creating [AiutaShoesMode] instances.
     *
     * This builder ensures that all required components are provided before
     * creating the mode instance.
     */
    @AiutaDsl
    public class Builder {
        public var onboardingShoesPage: AiutaShoesModeOnboardingPage? = null
        public var imagePicker: AiutaShoesModeImagePicker? = null

        public fun build(): AiutaShoesMode {
            val parentClass = "AiutaShoesMode"

            return AiutaShoesMode(
                onboardingShoesPage = onboardingShoesPage.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "onboardingShoesPage",
                ),
                imagePicker = imagePicker.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "imagePicker",
                ),
            )
        }
    }
}

/**
 * DSL function for configuring the shoes mode.
 *
 * Example usage:
 * ```
 * modes {
 *     shoes {
 *         onboardingShoesPage { ... }
 *         imagePicker { ... }
 *     }
 * }
 * ```
 */
public inline fun AiutaModes.Builder.shoes(
    block: AiutaShoesMode.Builder.() -> Unit,
): AiutaModes.Builder = apply {
    shoes = AiutaShoesMode.Builder().apply(block).build()
}
