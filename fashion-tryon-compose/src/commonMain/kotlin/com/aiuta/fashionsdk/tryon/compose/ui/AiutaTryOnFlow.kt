package com.aiuta.fashionsdk.tryon.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendSessionEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationContainer
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationInitialisation
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.disableSharingState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.isSharingEnable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.isTransitionActive
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.ZoomedImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen

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
    NavigationInitialisation(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        productItem = productForGeneration,
    ) {
        sendSessionEvent(AiutaAnalyticsSessionEvent.FlowType.TRY_ON)

        val controller = LocalController.current
        val scope = rememberCoroutineScope()

        NavigationContainer(modifier = modifier)

        // Move screen here, because full view should be on the top of navigation
        with(controller) {
            if (zoomImageController.isSharingEnable()) {
                ZoomedImageScreen(
                    modifier = modifier,
                    screenState = zoomImageController,
                    onTransitionFinished = {
                        if (!zoomImageController.isTransitionActive()) {
                            zoomImageController.disableSharingState()
                        }
                    },
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
