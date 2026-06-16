package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPickerEventType
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPickerAnalytic
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models.ShoesModelSelectorScreenAction

@Composable
internal fun ShoesModelSelectorScreen(modifier: Modifier = Modifier) {
    val dataController = LocalAiutaTryOnDataController.current
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val logger = LocalAiutaLogger.current

    val predefinedModelFeature = strictProvideFeature<AiutaImagePickerPredefinedModelFeature>()

    // Shoes-specific config — the mapper digs the view categories out of it
    val configuration = LocalAiutaConfiguration.current

    val viewModel = viewModel {
        ShoesModelSelectorViewModel(
            predefinedModelFeature = predefinedModelFeature,
            shoesMode = configuration.modes.shoes,
            dataController = dataController,
            controller = controller,
            logger = logger,
        )
    }

    val viewState = viewModel.viewState.collectAsStateWithLifecycle()
    val viewAction = viewModel.viewAction.collectAsStateWithLifecycle()

    sendPickerAnalytic(
        event = AiutaAnalyticsPickerEventType.PREDEFINED_MODELS_OPENED,
        pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
    )

    LaunchedEffect(viewAction.value) {
        when (viewAction.value) {
            is ShoesModelSelectorScreenAction.NavigateBack -> {
                navigationController.navigateBack()
                viewModel.clearAction()
            }

            is ShoesModelSelectorScreenAction.NavigateToImageSelector -> {
                navigationController.navigateTo(TryOnScreen.ImageSelector)
                viewModel.clearAction()
            }

            null -> Unit
        }
    }

    ShoesModelSelectorScreenContent(
        viewState = viewState,
        eventHandler = viewModel::obtainEvent,
        modifier = modifier,
    )
}
