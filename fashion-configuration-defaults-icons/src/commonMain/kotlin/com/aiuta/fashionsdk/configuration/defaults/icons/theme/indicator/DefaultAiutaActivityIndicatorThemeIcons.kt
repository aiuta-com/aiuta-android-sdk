package com.aiuta.fashionsdk.configuration.defaults.icons.theme.indicator

import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.icons.AiutaActivityIndicatorThemeIcons

/**
 * Default implementation of [AiutaActivityIndicatorThemeIcons].
 *
 * This class provides the default icon resources for the try-on activity indicator theme.
 * Currently, no loading icon is provided by default.
 *
 * @property loading14 14x14 pixel loading icon (null by default)
 */
public class DefaultAiutaActivityIndicatorThemeIcons : AiutaActivityIndicatorThemeIcons {
    override val loading14: AiutaIcon? = null
}
