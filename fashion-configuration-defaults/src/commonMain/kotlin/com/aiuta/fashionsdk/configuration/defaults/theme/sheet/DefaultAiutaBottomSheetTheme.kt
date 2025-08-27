package com.aiuta.fashionsdk.configuration.defaults.theme.sheet

import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.bottomSheet
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.grabber.AiutaBottomSheetThemeGrabber
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.shapes.AiutaBottomSheetThemeShapes
import com.aiuta.fashionsdk.configuration.ui.theme.sheet.toggles.AiutaBottomSheetThemeToggles

/**
 * Configures the default bottom sheet theme for the Aiuta SDK.
 *
 * This function sets up the bottom sheet with default typography, shapes, grabber, and toggles.
 *
 * @return The updated [AiutaTheme.Builder] instance.
 */
public fun AiutaTheme.Builder.defaultBottomSheet(): AiutaTheme.Builder = bottomSheet {
    shapes = AiutaBottomSheetThemeShapes.Default()
    grabber = AiutaBottomSheetThemeGrabber.Default()
    toggles = AiutaBottomSheetThemeToggles.Default()
}
