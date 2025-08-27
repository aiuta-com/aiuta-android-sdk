package com.aiuta.fashionsdk.configuration.defaults.theme.powerby

import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.powerby.poweredBar
import com.aiuta.fashionsdk.configuration.ui.theme.powerby.strings.AiutaPowerBarThemeStrings

/**
 * Configures the default powered by bar feature for the Aiuta SDK.
 *
 * This function sets up the powered by bar with default strings and colors.
 *
 * @return The updated [AiutaTheme.Builder] instance.
 */
public fun AiutaTheme.Builder.defaultPoweredBar(): AiutaTheme.Builder = poweredBar {
    strings = AiutaPowerBarThemeStrings.Default()
}
