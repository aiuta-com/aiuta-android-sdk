package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.configuration.features.tryon.validation.AiutaTryOnInputImageValidationFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaDialogController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.disableAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun GenerateMoreListener() {
    val coilContext = LocalPlatformContext.current
    val controller = LocalController.current
    val features = LocalAiutaFeatures.current
    val dialogController = LocalAiutaDialogController.current

    val inputImageValidationFeature = strictProvideFeature<AiutaTryOnInputImageValidationFeature>()

    // Wait for bottom sheet changes and start generation
    LaunchedEffect(controller.isAutoTryOnEnabled.value) {
        if (controller.isAutoTryOnEnabled.value) {
            controller.disableAutoTryOn()
            controller.startGeneration(
                coilContext = coilContext,
                dialogController = dialogController,
                features = features,
                inputImageValidationStrings = inputImageValidationFeature.strings,
            )
            controller.navigateBack()
        }
    }
}
