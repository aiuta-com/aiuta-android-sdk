package com.aiuta.fashionsdk.tryon.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationFlow
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.defaultStartScreen

/**
 * Entry point for the fashion try-on flow.
 *
 * This composable function initializes and manages the try-on experience.
 *
 * @param modifier The modifier to be applied to the layout
 * @param aiutaConfiguration The configuration for the Aiuta SDK
 * @param productForGeneration The product item to be used for try-on generation
 *
 * @see AiutaConfiguration
 * @see ProductItem
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun AiutaTryOnFlow(
    modifier: Modifier = Modifier,
    aiutaConfiguration: AiutaConfiguration,
    productForGeneration: ProductItem,
) {
    NavigationFlow(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        productItem = productForGeneration,
        startScreen = defaultStartScreen(),
        flowType = AiutaAnalyticsSessionEvent.FlowType.TRY_ON,
    )
}
