package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendSessionEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.isSharingEnable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.ZoomedImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun NavigationFlow(
    modifier: Modifier = Modifier,
    aiutaConfiguration: AiutaConfiguration,
    productConfiguration: ProductConfiguration,
    startScreen: NavigationScreen,
    // Analytics
    flowType: AiutaAnalyticsSessionEvent.FlowType,
) {
    NavigationInitialisation(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        startScreen = startScreen,
        productConfiguration = productConfiguration,
    ) {
        sendSessionEvent(flowType)

        val controller = LocalController.current
        val scope = rememberCoroutineScope()

        NavigationContainer(modifier = modifier)

        // Move screen here, because full view should be on the top of navigation
        with(controller) {
            if (zoomImageController.isSharingEnable()) {
                ZoomedImageScreen(
                    modifier = modifier,
                    screenState = zoomImageController,
                )
            }
        }

        BackHandler {
            when {
                controller.zoomImageController.isSharingEnable() -> {
                    controller.zoomImageController.closeZoomImageScreen(scope)
                }

                controller.bottomSheetNavigator.sheetState.isVisible -> {
                    controller.bottomSheetNavigator.hide()
                }

                controller.currentScreen.value == NavigationScreen.History -> {
                    // Use custom, because we need deactivate select changePhotoButtonStyle first
                    controller.deactivateSelectMode()
                    controller.navigateBack()
                }

                else -> controller.navigateBack()
            }
        }
    }
}

/**
 * Default product item used for initialization.
 *
 * This is an empty product item with default values, used as a placeholder
 * when initializing the history flow.
 */
internal val DefaultProductConfiguration by lazy {
    ProductConfiguration(
        productsForGeneration = listOf(
            ProductItem(
                id = "",
                title = "",
                imageUrls = emptyList(),
                brand = "",
            ),
        ),
    )
}
