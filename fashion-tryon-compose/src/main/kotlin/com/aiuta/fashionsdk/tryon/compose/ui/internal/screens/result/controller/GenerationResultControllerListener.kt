package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.aiuta.fashionsdk.internal.analytic.model.StartEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnDialogController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnStringResources
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.disableAutoTryOn
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration

@Composable
internal fun GenerateMoreListener() {
    val context = LocalContext.current
    val controller = LocalController.current
    val aiutaConfiguration = LocalAiutaConfiguration.current
    val dialogController = LocalAiutaTryOnDialogController.current
    val stringResources = LocalAiutaTryOnStringResources.current

    // Wait for bottom sheet changes and start generation
    LaunchedEffect(controller.isAutoTryOnEnabled.value) {
        if (controller.isAutoTryOnEnabled.value) {
            controller.disableAutoTryOn()
            controller.startGeneration(
                aiutaConfiguration = aiutaConfiguration,
                context = context,
                dialogController = dialogController,
                stringResources = stringResources,
                origin = StartEvent.TryOnOrigin.RETAKE_BUTTON,
            )
            controller.navigateBack()
        }
    }
}
