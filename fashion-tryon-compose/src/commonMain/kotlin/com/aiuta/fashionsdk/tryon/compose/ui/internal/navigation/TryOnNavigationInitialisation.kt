package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationInitialisation
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.tryon.compose.domain.models.ProductConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaFeatures
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
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val controller = rememberFashionTryOnController(
            aiutaConfiguration = aiutaConfiguration,
            productConfiguration = productConfiguration,
            startScreen = startScreen,
        )

        // Base init
        AiutaNavigationInitialisation(
            startScreen = startScreen,
            aiutaConfiguration = aiutaConfiguration,
            clickClose = {
                // TODO Add close with analytic
                // See clickClose in tryon-compose module
            },
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
            // Feature init
            CompositionLocalProvider(
                LocalAnalytic provides controller.analytic,
                LocalController provides controller,
                LocalAiutaFeatures provides aiutaConfiguration.features,
                LocalAiutaTryOnDataController provides rememberAiutaTryOnDataController(
                    aiuta = { aiutaConfiguration.aiuta },
                ),
                LocalAiutaTryOnLoadingActionsController provides rememberAiutaTryOnLoadingActionsController(),
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
