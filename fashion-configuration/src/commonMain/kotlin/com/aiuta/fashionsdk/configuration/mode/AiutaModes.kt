package com.aiuta.fashionsdk.configuration.mode

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.annotations.AiutaDsl
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.mode.shoes.AiutaShoesMode

/**
 * Container for per-mode configuration overrides of the Aiuta SDK.
 *
 * Modes let integrators tailor specific surfaces (onboarding, image picker, ...)
 * for a particular try-on scenario. Each mode is optional: a `null` value means
 * the corresponding mode is not configured and the default behavior applies.
 *
 * @property shoes Optional overrides for the shoes try-on mode. `null` when not configured.
 * @see AiutaShoesMode
 */
@Immutable
public class AiutaModes(
    public val shoes: AiutaShoesMode? = null,
) {
    /**
     * Builder class for creating [AiutaModes] instances with DSL-style configuration.
     */
    @AiutaDsl
    public class Builder {
        /**
         * Optional shoes mode configuration.
         */
        public var shoes: AiutaShoesMode? = null

        public fun build(): AiutaModes = AiutaModes(
            shoes = shoes,
        )
    }
}

/**
 * Extension function for configuring modes within an [AiutaConfiguration.Builder].
 *
 * Example usage:
 * ```
 * modes {
 *     shoes {
 *         ...
 *     }
 * }
 * ```
 *
 * @param block Configuration block for setting up modes
 * @return The configuration builder for method chaining
 * @see AiutaConfiguration.Builder
 * @see AiutaModes.Builder
 */
public inline fun AiutaConfiguration.Builder.modes(
    block: AiutaModes.Builder.() -> Unit,
): AiutaConfiguration.Builder = apply {
    modes = AiutaModes.Builder().apply(block).build()
}
