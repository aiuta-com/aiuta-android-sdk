package com.aiuta.fashionsdk.configuration.ui.theme.sheet

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.configuration.internal.utils.checkNotNullWithDescription
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.grabber.AiutaBottomSheetThemeGrabber
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.shapes.AiutaBottomSheetThemeShapes
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.toggles.AiutaBottomSheetThemeToggles

/**
 * Bottom sheet theme configuration for the Aiuta SDK.
 *
 * This immutable class defines the visual styling for bottom sheets throughout
 * the SDK, including typography, shapes, grabber, and toggle configurations.
 * It provides a consistent bottom sheet appearance that can be customized to
 * match your app's design system.
 *
 *
 * @property shapes Shape configuration for bottom sheet containers
 * @property grabber Configuration for the bottom sheet drag handle
 * @property toggles Configuration for bottom sheet behavior toggles
 * @see AiutaBottomSheetThemeShapes
 * @see AiutaBottomSheetThemeGrabber
 * @see AiutaBottomSheetThemeToggles
 */
@Immutable
public class AiutaBottomSheetTheme(
    public val shapes: AiutaBottomSheetThemeShapes,
    public val grabber: AiutaBottomSheetThemeGrabber,
    public val toggles: AiutaBottomSheetThemeToggles,
) {
    /**
     * Builder class for creating [AiutaBottomSheetTheme] instances.
     *
     * This builder ensures all required theme components are set before creating
     * the final bottom sheet theme configuration.
     */
    public class Builder {
        /**
         * Shape configuration for bottom sheet containers.
         */
        public var shapes: AiutaBottomSheetThemeShapes? = null

        /**
         * Configuration for the bottom sheet drag handle.
         */
        public var grabber: AiutaBottomSheetThemeGrabber? = null

        /**
         * Configuration for bottom sheet behavior toggles.
         */
        public var toggles: AiutaBottomSheetThemeToggles? = null

        /**
         * Creates an [AiutaBottomSheetTheme] instance with the configured properties.
         *
         * @return Configured [AiutaBottomSheetTheme] instance
         * @throws IllegalArgumentException if required properties are not set
         */
        public fun build(): AiutaBottomSheetTheme {
            val parentClass = "AiutaBottomSheetTheme"

            return AiutaBottomSheetTheme(
                shapes = shapes.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "shapes",
                ),
                grabber = grabber.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "grabber",
                ),
                toggles = toggles.checkNotNullWithDescription(
                    parentClass = parentClass,
                    property = "styles",
                ),
            )
        }
    }
}

/**
 * Extension function for configuring bottom sheet theme within an [AiutaTheme.Builder].
 *
 * This extension provides a convenient DSL for configuring the bottom sheet theme
 * as part of the main theme setup.
 *
 * ```kotlin
 * theme {
 *     bottomSheet {
 *         typography = AiutaBottomSheetThemeTypography.Default()
 *         shapes = AiutaBottomSheetThemeShapes.Default()
 *         grabber = AiutaBottomSheetThemeGrabber.Default()
 *         toggles = AiutaBottomSheetThemeToggles.Default()
 *     }
 *     // Configure other theme components...
 * }
 * ```
 *
 * @param block Configuration block for setting up the bottom sheet theme
 * @return The theme builder for method chaining
 * @see AiutaTheme.Builder
 * @see AiutaBottomSheetTheme.Builder
 */
public inline fun AiutaTheme.Builder.bottomSheet(
    block: AiutaBottomSheetTheme.Builder.() -> Unit,
): AiutaTheme.Builder = apply {
    bottomSheet = AiutaBottomSheetTheme.Builder().apply(block).build()
}
