package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenAction
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.utils.GenerateMoreListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.utils.ResultFeedbackSheetListener

@Composable
internal fun GenerationResultScreen(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val features = LocalAiutaFeatures.current
    val logger = LocalAiutaLogger.current

    sendPageEvent(pageId = AiutaAnalyticsPageId.RESULTS)

    val viewModel = viewModel {
        GenerationResultViewModel(
            features = features,
            controller = controller,
            sessionGenerationInteractor = controller.sessionGenerationInteractor,
            generatedOperationInteractor = controller.generatedOperationInteractor,
            logger = logger,
        )
    }

    val viewState = viewModel.viewState.collectAsStateWithLifecycle()
    val viewAction = viewModel.viewAction.collectAsStateWithLifecycle()

    // Auto try-on -> startGeneration -> back (moved from controller/, kept as-is)
    GenerateMoreListener()

    // Show the "thanks" overlay once a feedback sheet closes (ported out of ThanksFeedbackBlock)
    ResultFeedbackSheetListener(eventHandler = viewModel::obtainEvent)

    LaunchedEffect(viewAction.value) {
        val action = viewAction.value

        when (action) {
            is GenerationResultScreenAction.NavigateBack -> {
                navigationController.navigateBack()
            }

            is GenerationResultScreenAction.ShowFeedbackSheet -> {
                bottomSheetNavigator.show(
                    newSheetScreen = TryOnBottomSheetScreen.Feedback(productIds = action.productIds),
                )
            }

            is GenerationResultScreenAction.ShowFitDisclaimerSheet -> {
                bottomSheetNavigator.show(newSheetScreen = TryOnBottomSheetScreen.FitDisclaimer)
            }

            is GenerationResultScreenAction.ShowChangePhotoSheet -> {
                bottomSheetNavigator.show(
                    newSheetScreen = if (action.hasMultipleOperations) {
                        TryOnBottomSheetScreen.GeneratedOperations
                    } else {
                        TryOnBottomSheetScreen.ImagePicker(originPageId = AiutaAnalyticsPageId.RESULTS)
                    },
                )
            }

            is GenerationResultScreenAction.ShowProductInfoSheet -> {
                bottomSheetNavigator.show(
                    newSheetScreen = TryOnBottomSheetScreen.ProductInfo(
                        primaryButtonState = TryOnBottomSheetScreen.ProductInfo.PrimaryButtonState.ADD_TO_CART,
                        originPageId = AiutaAnalyticsPageId.RESULTS,
                        productItem = action.product,
                    ),
                )
            }

            null -> Unit
        }

        if (action != null) {
            viewModel.clearAction()
        }
    }

    GenerationResultScreenContent(
        viewState = viewState,
        eventHandler = viewModel::obtainEvent,
        modifier = modifier,
    )
}
