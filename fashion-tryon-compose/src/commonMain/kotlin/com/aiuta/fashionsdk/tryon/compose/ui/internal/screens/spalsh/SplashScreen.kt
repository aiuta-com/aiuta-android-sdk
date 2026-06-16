package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDataController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.spalsh.models.SplashScreenAction

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateTo: (TryOnScreen) -> Unit,
) {
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dataController = LocalAiutaTryOnDataController.current
    val mode = LocalAiutaMode.current
    val onboardingShoesPage = LocalAiutaConfiguration.current.modes.shoes?.onboardingShoesPage

    val viewModel = viewModel {
        SplashViewModel(
            features = features,
            controller = controller,
            dataController = dataController,
            mode = mode,
            onboardingShoesPage = onboardingShoesPage,
        )
    }

    val viewAction = viewModel.viewAction.collectAsStateWithLifecycle()

    LaunchedEffect(viewAction.value) {
        when (val action = viewAction.value) {
            is SplashScreenAction.NavigateTo -> {
                navigateTo(action.screen)
                viewModel.clearAction()
            }

            null -> Unit
        }
    }

    Box(modifier = modifier)
}
