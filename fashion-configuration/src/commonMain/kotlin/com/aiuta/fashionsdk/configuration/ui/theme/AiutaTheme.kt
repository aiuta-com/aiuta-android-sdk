package com.aiuta.fashionsdk.configuration.ui.theme

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.ui.AiutaUserInterfaceConfiguration
import com.aiuta.fashionsdk.configuration.ui.theme.button.AiutaButtonTheme
import com.aiuta.fashionsdk.configuration.ui.theme.color.AiutaColorTheme
import com.aiuta.fashionsdk.configuration.ui.theme.error.AiutaErrorSnackbarTheme
import com.aiuta.fashionsdk.configuration.ui.theme.image.AiutaImageTheme
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.AiutaActivityIndicatorTheme
import com.aiuta.fashionsdk.configuration.ui.theme.label.AiutaLabelTheme
import com.aiuta.fashionsdk.configuration.ui.theme.pagebar.AiutaPageBarTheme
import com.aiuta.fashionsdk.configuration.ui.theme.powerby.AiutaPowerBarTheme
import com.aiuta.fashionsdk.configuration.ui.theme.productbar.AiutaProductBarTheme
import com.aiuta.fashionsdk.configuration.ui.theme.selection.AiutaSelectionSnackbarTheme
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.AiutaBottomSheetTheme

/**
 * Main theme configuration class for the Aiuta SDK.
 *
 * This immutable class contains all visual theming configuration for the SDK's
 * user interface components. It provides comprehensive customization options
 * for colors, typography, shapes, icons, and component-specific styling.
 *
 * The theme is organized into component-specific configurations:
 * - Colors and general styling
 * - Component-specific themes (buttons, images, labels, etc.)
 * - Layout and interaction themes (page bars, bottom sheets, etc.)
 * - Feedback and notification themes (snackbars, error messages)
 *
 * ```kotlin
 * val theme = AiutaTheme.Builder().apply {
 *     color = AiutaColorTheme.Default()
 *
 *     button {
 *         typography = AiutaButtonThemeTypography.Default()
 *         shapes = AiutaButtonThemeShapes.Default()
 *     }
 *
 *     image {
 *         shapes = AiutaImageThemeShapes.Default()
 *         icons = AiutaImageThemeIcons.Default()
 *     }
 *
 *     // Configure other theme components...
 * }.build()
 * ```
 *
 * @property color Global color theme configuration
 * @property label Text and label styling configuration
 * @property image Image display and icon configuration
 * @property button Button styling and interaction configuration
 * @property pageBar Page navigation bar configuration
 * @property powerBar "Powered by Aiuta" branding configuration
 * @property bottomSheet Bottom sheet styling configuration
 * @property selectionSnackbar Selection feedback snackbar configuration
 * @property errorSnackbar Error message snackbar configuration
 * @property productBar Product information bar configuration
 * @see AiutaColorTheme
 * @see AiutaButtonTheme
 * @see AiutaImageTheme
 */
@Immutable
public class AiutaTheme(
    public val color: AiutaColorTheme,
    public val label: AiutaLabelTheme,
    public val image: AiutaImageTheme,
    public val button: AiutaButtonTheme,
    public val pageBar: AiutaPageBarTheme,
    public val powerBar: AiutaPowerBarTheme,
    public val bottomSheet: AiutaBottomSheetTheme,
    public val activityIndicator: AiutaActivityIndicatorTheme,
    public val selectionSnackbar: AiutaSelectionSnackbarTheme,
    public val errorSnackbar: AiutaErrorSnackbarTheme,
    public val productBar: AiutaProductBarTheme,
) {
    /**
     * Builder class for creating [AiutaTheme] instances.
     *
     * This builder ensures all required theme components are configured before
     * creating the final theme instance. Each theme component must be set
     * to create a complete theme configuration.
     */
    public class Builder {
        /**
         * Global color theme configuration.
         */
        public var color: AiutaColorTheme? = null

        /**
         * Text and label styling configuration.
         */
        public var label: AiutaLabelTheme? = null

        /**
         * Image display and icon configuration.
         */
        public var image: AiutaImageTheme? = null

        /**
         * Button styling and interaction configuration.
         */
        public var button: AiutaButtonTheme? = null

        /**
         * Page navigation bar configuration.
         */
        public var pageBar: AiutaPageBarTheme? = null

        /**
         * "Powered by Aiuta" branding configuration.
         */
        public var powerBar: AiutaPowerBarTheme? = null

        /**
         * Bottom sheet styling configuration.
         */
        public var bottomSheet: AiutaBottomSheetTheme? = null

        /**
         * Activity indicator configuration.
         */
        public var activityIndicator: AiutaActivityIndicatorTheme? = null

        /**
         * Selection feedback snackbar configuration.
         */
        public var selectionSnackbar: AiutaSelectionSnackbarTheme? = null

        /**
         * Error message snackbar configuration.
         */
        public var errorSnackbar: AiutaErrorSnackbarTheme? = null

        /**
         * Product information bar configuration.
         */
        public var productBar: AiutaProductBarTheme? = null

        /**
         * Creates an [AiutaTheme] instance with the configured properties.
         *
         * This method validates that all required theme components are set
         * before creating the final theme configuration.
         *
         * @return Configured [AiutaTheme] instance
         * @throws IllegalArgumentException if required theme components are not set
         */
        public fun build(): AiutaTheme {
            val parentClass = "AiutaTheme"

            return AiutaTheme(
                color = color.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "color",
                ),
                label = label.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "label",
                ),
                image = image.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "image",
                ),
                button = button.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "button",
                ),
                pageBar = pageBar.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "pageBar",
                ),
                powerBar = powerBar.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "powerBar",
                ),
                bottomSheet = bottomSheet.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "bottomSheet",
                ),
                activityIndicator = activityIndicator.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "activityIndicator",
                ),
                selectionSnackbar = selectionSnackbar.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "selectionSnackbar",
                ),
                errorSnackbar = errorSnackbar.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "errorSnackbar",
                ),
                productBar = productBar.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "productBar",
                ),
            )
        }
    }
}

/**
 * Extension function for configuring theme within an [AiutaUserInterfaceConfiguration.Builder].
 *
 * This extension provides a convenient DSL for configuring the theme as part of
 * the UI configuration setup.
 *
 * ```kotlin
 * val uiConfig = AiutaUserInterfaceConfiguration.Builder().apply {
 *     theme {
 *         color = AiutaColorTheme.Default()
 *
 *         button {
 *             typography = AiutaButtonThemeTypography.Default()
 *             shapes = AiutaButtonThemeShapes.Default()
 *         }
 *
 *         image {
 *             shapes = AiutaImageThemeShapes.Default()
 *             icons = AiutaImageThemeIcons.Default()
 *         }
 *
 *         // Configure other theme components...
 *     }
 *
 *     actions = myActionHandler
 * }.build()
 * ```
 *
 * @param block Configuration block for setting up the theme
 * @return The UI configuration builder for method chaining
 * @see AiutaUserInterfaceConfiguration.Builder
 * @see AiutaTheme.Builder
 */
public inline fun AiutaUserInterfaceConfiguration.Builder.theme(
    block: AiutaTheme.Builder.() -> Unit,
): AiutaUserInterfaceConfiguration.Builder = apply {
    theme = AiutaTheme.Builder().apply(block).build()
}
