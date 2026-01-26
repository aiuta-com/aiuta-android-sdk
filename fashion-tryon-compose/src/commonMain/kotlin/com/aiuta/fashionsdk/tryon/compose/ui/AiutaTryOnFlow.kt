package com.aiuta.fashionsdk.tryon.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnNavigationFlow
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.defaultStartScreen

/**
 * Entry point for the fashion try-on flow.
 *
 * This composable function initializes and manages the try-on experience.
 *
 * @param modifier The modifier to be applied to the layout
 * @param aiutaConfiguration The configuration for the Aiuta SDK
 * @param productConfiguration The product item to be used for try-on generation
 *
 * @see AiutaConfiguration
 * @see ProductItem
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun AiutaTryOnFlow(
    modifier: Modifier = Modifier,
    aiutaConfiguration: AiutaConfiguration,
    productConfiguration: ProductConfiguration,
) {
    TryOnNavigationFlow(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        productConfiguration = productConfiguration,
        startScreen = defaultStartScreen(),
        flowType = AiutaAnalyticsSessionEvent.FlowType.TRY_ON,
    )
}

/**
 * Entry point for the fashion try-on flow.
 *
 * This composable function initializes and manages the try-on experience
 *
 * [Deprecated] Use [AiutaTryOnFlow] with `productConfiguration: ProductConfiguration` instead:
 * `AiutaTryOnFlow(productConfiguration = ProductConfiguration(productsForGeneration = listOf(yourProduct)))`.
 *
 * @param modifier The modifier to be applied to the layout
 * @param aiutaConfiguration The configuration for the Aiuta SDK
 * @param productForGeneration The product item to be used for try-on generation
 *
 * @see AiutaConfiguration
 * @see ProductItem
 */
@Deprecated(
    message = "Use AiutaTryOnFlow(productConfiguration = ProductConfiguration(...)) instead",
    replaceWith = ReplaceWith(
        expression = "AiutaTryOnFlow(modifier, aiutaConfiguration, productConfiguration = ProductConfiguration(productsForGeneration = listOf(productForGeneration)))",
        imports = [
            "com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration",
        ],
    ),
)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
public fun AiutaTryOnFlow(
    modifier: Modifier = Modifier,
    aiutaConfiguration: AiutaConfiguration,
    productForGeneration: ProductItem,
) {
    TryOnNavigationFlow(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        productConfiguration = ProductConfiguration(
            productsForGeneration = listOf(productForGeneration),
        ),
        startScreen = defaultStartScreen(),
        flowType = AiutaAnalyticsSessionEvent.FlowType.TRY_ON,
    )
}
