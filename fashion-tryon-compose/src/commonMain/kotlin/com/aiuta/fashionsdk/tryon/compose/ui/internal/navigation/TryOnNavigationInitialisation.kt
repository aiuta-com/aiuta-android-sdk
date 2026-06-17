package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationInitialisation
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data.rememberAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.deletingGeneratedImagesListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.deletingUploadedImagesListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.rememberAiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.rememberFashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.disclaimer.FitDisclaimerSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.feedback.ExtraFeedbackSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.feedback.FeedbackSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.operations.GeneratedOperationsSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.picker.ImagePickerSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.ProductInfoSheet

@Composable
internal fun TryOnNavigationInitialisation(
    aiutaConfiguration: AiutaConfiguration,
    productConfiguration: ProductConfiguration,
    startScreen: TryOnScreen,
    mode: AiutaMode,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        // Base init
        AiutaNavigationInitialisation(
            startScreen = startScreen,
            aiutaConfiguration = aiutaConfiguration,
            bottomSheetEntryProvider = aiutaEntryProvider {
                aiutaEntry<TryOnBottomSheetScreen.ImagePicker> { key ->
                    ImagePickerSheet(pickerData = key)
                }
                aiutaEntry<TryOnBottomSheetScreen.ProductInfo> { key ->
                    ProductInfoSheet(productInfo = key)
                }
                aiutaEntry<TryOnBottomSheetScreen.Feedback> { key ->
                    FeedbackSheet(args = key)
                }
                aiutaEntry<TryOnBottomSheetScreen.ExtraFeedback> { key ->
                    ExtraFeedbackSheet(data = key)
                }
                aiutaEntry<TryOnBottomSheetScreen.FitDisclaimer> {
                    FitDisclaimerSheet()
                }
                aiutaEntry<TryOnBottomSheetScreen.GeneratedOperations> {
                    GeneratedOperationsSheet()
                }
            },
        ) {
            val controller = rememberFashionTryOnController(
                aiutaConfiguration = aiutaConfiguration,
                productConfiguration = productConfiguration,
            )

            // Feature init
            CompositionLocalProvider(
                LocalAnalytic provides controller.analytic,
                LocalController provides controller,
                LocalAiutaTryOnDataController provides rememberAiutaTryOnDataController(
                    aiuta = { aiutaConfiguration.aiuta },
                ),
                LocalAiutaTryOnLoadingActionsController provides rememberAiutaTryOnLoadingActionsController(),
                LocalAiutaMode provides resolveAiutaMode(
                    productConfiguration = productConfiguration,
                    mode = mode,
                ),
            ) {
                // Init listeners
                val loadingActionsController = LocalAiutaTryOnLoadingActionsController.current
                loadingActionsController.deletingGeneratedImagesListener()
                loadingActionsController.deletingUploadedImagesListener(controller)

                // Actual content
                content()
            }
        }
    }
}

/**
 * Resolves the final [AiutaMode] that should be provided to [LocalAiutaMode].
 *
 * When more than one product is requested for generation, the flow always runs
 * in [AiutaMode.GENERAL]; otherwise the externally provided [mode] is used.
 */
private fun resolveAiutaMode(
    productConfiguration: ProductConfiguration,
    mode: AiutaMode,
): AiutaMode = if (productConfiguration.productsForGeneration.size > 1) {
    AiutaMode.GENERAL
} else {
    mode
}
