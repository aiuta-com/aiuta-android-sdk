package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController

@Composable
internal fun GenerationResultListener() {
    GenerationResultSessionListener()
}

@Composable
private fun GenerationResultSessionListener() {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current

    // If we delete all session images, we should navigate back to picker
    LaunchedEffect(controller.sessionGenerationInteractor.sessionGenerations.size) {
        if (controller.sessionGenerationInteractor.sessionGenerations.isEmpty()) {
            navigationController.navigateBack()
        }
    }
}
