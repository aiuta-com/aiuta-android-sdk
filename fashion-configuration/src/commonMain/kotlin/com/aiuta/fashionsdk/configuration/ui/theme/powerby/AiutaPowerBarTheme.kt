package com.aiuta.fashionsdk.configuration.ui.theme.powerby

import com.aiuta.fashionsdk.configuration.features.AiutaFeature
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.powerby.strings.AiutaPowerBarThemeStrings

/**
 * Power bar theme configuration for the Aiuta SDK.
 *
 * This class defines the visual styling for the "Powered by Aiuta" branding bar
 * throughout the SDK, including text content and color configurations. It provides
 * a consistent branding appearance that can be customized to match your app's design.
 *
 *
 * @property strings Text content configuration for the power bar
 * @see AiutaPowerBarThemeStrings
 */
public class AiutaPowerBarTheme(
    public val strings: AiutaPowerBarThemeStrings,
) : AiutaFeature {

    /**
     * Builder class for creating [AiutaPowerBarTheme] instances.
     *
     * This builder ensures all required theme components are set before creating
     * the final power bar theme configuration.
     */
    public class Builder : AiutaFeature.Builder {
        /**
         * Text content configuration for the power bar.
         */
        public var strings: AiutaPowerBarThemeStrings? = null

        /**
         * Creates an [AiutaPowerBarTheme] instance with the configured properties.
         *
         * @return Configured [AiutaPowerBarTheme] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public override fun build(): AiutaPowerBarTheme {
            val parentClass = "AiutaPowerBarTheme"

            return AiutaPowerBarTheme(
                strings = strings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "strings",
                ),
            )
        }
    }
}

/**
 * Extension function for configuring power bar theme within an [AiutaTheme.Builder].
 *
 * This extension provides a convenient DSL for configuring the power bar theme
 * as part of the main theme setup.
 *
 * ```kotlin
 * theme {
 *     poweredBar {
 *         strings = AiutaPowerBarThemeStrings.Default()
 *         colors = AiutaPowerBarThemeColors.Default()
 *     }
 * }
 * ```
 *
 * @param block Configuration block for setting up the power bar theme
 * @return The theme builder for method chaining
 * @see AiutaTheme.Builder
 * @see AiutaPowerBarTheme.Builder
 */
public inline fun AiutaTheme.Builder.poweredBar(
    block: AiutaPowerBarTheme.Builder.() -> Unit,
): AiutaTheme.Builder = apply {
    powerBar = AiutaPowerBarTheme.Builder().apply(block).build()
}
