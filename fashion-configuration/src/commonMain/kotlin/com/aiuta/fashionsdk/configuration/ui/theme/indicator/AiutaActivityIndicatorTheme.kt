package com.aiuta.fashionsdk.configuration.ui.theme.indicator

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.colors.AiutaActivityIndicatorThemeColors
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.icons.AiutaActivityIndicatorThemeIcons
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.settings.AiutaActivityIndicatorThemeSettings

/**
 * Represents the theme configuration for the Aiuta Activity Indicator component.
 *
 * @property icons The icon set used for the activity indicator.
 * @property colors The color scheme for the activity indicator.
 * @property settings Additional settings for the activity indicator.
 * @see AiutaActivityIndicatorThemeIcons
 * @see AiutaActivityIndicatorThemeColors
 * @see AiutaActivityIndicatorThemeSettings
 */
@Immutable
public class AiutaActivityIndicatorTheme(
    public val icons: AiutaActivityIndicatorThemeIcons,
    public val colors: AiutaActivityIndicatorThemeColors,
    public val settings: AiutaActivityIndicatorThemeSettings,
) {
    /**
     * Builder for [AiutaActivityIndicatorTheme].
     *
     * Allows step-by-step configuration of the theme properties before building an immutable instance.
     */
    public class Builder {

        /**
         * The icon set to be used for the activity indicator.
         */
        public var icons: AiutaActivityIndicatorThemeIcons? = null

        /**
         * The color scheme to be used for the activity indicator.
         */
        public var colors: AiutaActivityIndicatorThemeColors? = null

        /**
         * Additional settings for the activity indicator.
         */
        public var settings: AiutaActivityIndicatorThemeSettings? = null

        /**
         * Creates an [AiutaActivityIndicatorTheme] instance with the configured properties.
         *
         * @return Configured [AiutaActivityIndicatorTheme] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public fun build(): AiutaActivityIndicatorTheme {
            val parentClass = "AiutaActivityIndicatorTheme"

            return AiutaActivityIndicatorTheme(
                icons = icons.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "icons",
                ),
                colors = colors.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "colors",
                ),
                settings = settings.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "settings",
                ),
            )
        }
    }
}

/**
 * Configures the [AiutaActivityIndicatorTheme] for the [AiutaTheme.Builder].
 *
 * @param block Lambda to configure the [AiutaActivityIndicatorTheme.Builder].
 * @return The [AiutaTheme.Builder] with the activity indicator theme applied.
 */
public inline fun AiutaTheme.Builder.activityIndicator(
    block: AiutaActivityIndicatorTheme.Builder.() -> Unit,
): AiutaTheme.Builder = apply {
    activityIndicator = AiutaActivityIndicatorTheme.Builder().apply(block).build()
}
