package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.configuration.features.tryon.validation.AiutaTryOnInputImageValidationFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.disableAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun ImageSelectorAutoTryOnListener() {
    val coilContext = LocalPlatformContext.current
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dialogController = LocalAiutaTryOnDialogController.current

    val inputImageValidationFeature = strictProvideFeature<AiutaTryOnInputImageValidationFeature>()

    LaunchedEffect(controller.isAutoTryOnEnabled.value) {
        if (controller.isAutoTryOnEnabled.value) {
            controller.disableAutoTryOn()
            controller.startGeneration(
                coilContext = coilContext,
                dialogController = dialogController,
                features = features,
                inputImageValidationStrings = inputImageValidationFeature.strings,
            )
        }
    }
}
