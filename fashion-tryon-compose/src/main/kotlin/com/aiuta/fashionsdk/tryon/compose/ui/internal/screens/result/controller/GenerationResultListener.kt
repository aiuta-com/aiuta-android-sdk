package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack

@Composable
internal fun GenerationResultListener() {
    GenerationResultSessionListener()
}

@Composable
private fun GenerationResultSessionListener() {
    val controller = LocalController.current

    // If we delete all session images, we should navigate back to picker
    LaunchedEffect(controller.sessionGenerationInteractor.sessionGenerations.size) {
        if (controller.sessionGenerationInteractor.sessionGenerations.isEmpty()) {
            controller.navigateBack()
        }
    }
}
