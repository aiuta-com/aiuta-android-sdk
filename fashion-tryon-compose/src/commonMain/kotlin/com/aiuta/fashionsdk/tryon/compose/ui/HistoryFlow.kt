package com.aiuta.fashionsdk.tryon.compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendSessionEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationInitialisation
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.isSharingEnable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.HistoryScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.ZoomedImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen
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
    NavigationInitialisation(
        modifier = modifier,
        aiutaConfiguration = aiutaConfiguration,
        productItem = DefaultProductItem,
    ) {
        sendSessionEvent(AiutaAnalyticsSessionEvent.FlowType.HISTORY)

        val scope = rememberCoroutineScope()
        val controller = LocalController.current

        LaunchedEffect(Unit) {
            controller.navigateTo(NavigationScreen.History)
        }

        HistoryScreen(modifier = Modifier.fillMaxSize())

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
internal val DefaultProductItem by lazy {
    ProductItem(
        id = "",
        description = "",
        imageUrls = emptyList(),
        localizedPrice = "",
        store = "",
    )
}
