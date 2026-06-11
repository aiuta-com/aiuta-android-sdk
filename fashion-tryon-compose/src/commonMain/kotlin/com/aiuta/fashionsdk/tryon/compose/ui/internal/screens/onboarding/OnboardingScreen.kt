package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenAction

@Composable
internal fun OnboardingScreen(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val features = LocalAiutaFeatures.current

    val viewModel = viewModel {
        OnboardingViewModel(
            features = features,
            controller = controller,
            onboardingInteractor = controller.onboardingInteractor,
            consentInteractor = controller.consentInteractor,
        )
    }

    val viewState = viewModel.viewState.collectAsStateWithLifecycle()
    val viewAction = viewModel.viewAction.collectAsStateWithLifecycle()

    LaunchedEffect(viewAction.value) {
        when (val action = viewAction.value) {
            is OnboardingScreenAction.NavigateBack -> {
                navigationController.navigateBack()
                viewModel.clearAction()
            }

            is OnboardingScreenAction.NavigateToImageSelector -> {
                navigationController.popUpAndNavigateTo(TryOnScreen.ImageSelector)
                viewModel.clearAction()
            }

            is OnboardingScreenAction.Close -> {
                controller.clickClose(
                    navigationController = navigationController,
                    pageId = action.pageId,
                )
                viewModel.clearAction()
            }

            null -> Unit
        }
    }

    OnboardingScreenContent(
        viewState = viewState,
        eventHandler = viewModel::obtainEvent,
        modifier = modifier,
    )
}
