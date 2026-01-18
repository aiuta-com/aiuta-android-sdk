package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsSessionEvent
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationFlow
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendSessionEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.isSharingEnable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.ConsentScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.HistoryScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.ImageListScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.ModelSelectorScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.OnboardingScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.preonboarding.PreOnboardingScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.GenerationResultScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.ImageSelectorScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh.SplashScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.ZoomedImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun TryOnNavigationFlow(
    aiutaConfiguration: AiutaConfiguration,
    productConfiguration: ProductConfiguration,
    startScreen: TryOnScreen,
    // Analytics
    flowType: AiutaAnalyticsSessionEvent.FlowType,
    modifier: Modifier = Modifier,
) {
    val sharedModifier = Modifier.fillMaxSize()

    TryOnNavigationInitialisation(
        aiutaConfiguration = aiutaConfiguration,
        startScreen = startScreen,
        productConfiguration = productConfiguration,
        modifier = modifier,
    ) {
        sendSessionEvent(flowType)

        val controller = LocalController.current
        val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
        val navigationController = LocalAiutaNavigationController.current
        val scope = rememberCoroutineScope()

        AiutaNavigationFlow(
            contentEntryProvider = aiutaEntryProvider {
                aiutaEntry<TryOnScreen.Splash> {
                    SplashScreen(
                        modifier = sharedModifier,
                        navigateTo = controller::navigateTo,
                    )
                }
                aiutaEntry<TryOnScreen.Preonboarding> {
                    PreOnboardingScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.Onboarding> {
                    OnboardingScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.History> {
                    HistoryScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.ImageSelector> {
                    ImageSelectorScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.Consent> { key ->
                    ConsentScreen(
                        modifier = sharedModifier,
                        onObtainedConsents = key.onObtainedConsents,
                    )
                }
                aiutaEntry<TryOnScreen.ModelSelector> {
                    ModelSelectorScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.GenerationResult> {
                    GenerationResultScreen(modifier = sharedModifier)
                }
                aiutaEntry<TryOnScreen.ImageListViewer> { key ->
                    ImageListScreen(
                        modifier = sharedModifier,
                        args = key,
                    )
                }
            },
        )

        // Move screen here, because full view should be on the top of navigation
        with(controller) {
            if (zoomImageController.isSharingEnable()) {
                ZoomedImageScreen(
                    screenState = zoomImageController,
                    modifier = sharedModifier,
                )
            }
        }

        BackHandler {
            when {
                controller.zoomImageController.isSharingEnable() -> {
                    controller.zoomImageController.closeZoomImageScreen(scope)
                }

                bottomSheetNavigator.isVisible -> {
                    bottomSheetNavigator.hide()
                }

                navigationController.currentScreen.value == TryOnScreen.History -> {
                    // Use custom, because we need deactivate select changePhotoButtonStyle first
                    controller.deactivateSelectMode()
                    controller.navigateBack()
                }

                else -> navigationController.navigateBack()
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
