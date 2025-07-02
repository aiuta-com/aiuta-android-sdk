package com.aiuta.fashionsdk.configuration.defaults.theme.indicator

import com.aiuta.fashionsdk.configuration.defaults.icons.theme.indicator.DefaultAiutaActivityIndicatorThemeIcons
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.activityIndicator
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.colors.AiutaActivityIndicatorThemeColors
import com.aiuta.fashionsdk.configuration.ui.theme.indicator.settings.AiutaActivityIndicatorThemeSettings

public fun AiutaTheme.Builder.defaultActivityIndicator(): AiutaTheme.Builder = activityIndicator {
    icons = DefaultAiutaActivityIndicatorThemeIcons()
    colors = AiutaActivityIndicatorThemeColors.Default()
    settings = AiutaActivityIndicatorThemeSettings.Default()
}
