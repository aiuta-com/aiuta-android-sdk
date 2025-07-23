package com.aiuta.fashionsdk.tryon.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.DefaultProductItem
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationFlow
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn

/**
 * Entry point for the history flow.
 *
 * This composable function initializes and manages the try-on history experience.
 *
 * @param modifier The modifier to be applied to the layout
 * @param aiutaConfiguration The configuration for the Aiuta SDK
 *
 * @see Aiuta
 * @see AiutaTryOn
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun HistoryFlow(
    modifier: Modifier = Modifier,
    aiutaConfiguration: AiutaConfiguration,
) {
    NavigationFlow(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        startScreen = NavigationScreen.History,
        productItem = DefaultProductItem,
        flowType = AiutaAnalyticsSessionEvent.FlowType.HISTORY,
    )
}
