package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.loading.AiutaTryOnLoadingPageFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationOperation
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import kotlinx.coroutines.delay

private const val LOADING_TEXT_DELAY = 2000L

@Composable
internal fun solveLoadingGenerationText(): State<String> {
    val controller = LocalController.current

    val operation = controller.generationOperations.lastOrNull()
    val loadingText = remember { mutableStateOf("") }
    val loadingPageFeature = strictProvideFeature<AiutaTryOnLoadingPageFeature>()

    LaunchedEffect(operation) {
        loadingText.value =
            when (operation) {
                is ProductGenerationOperation.LoadingOperation.GenerationProcessingOperation -> {
                    // Need to wait before showing ping status
                    delay(LOADING_TEXT_DELAY)
                    loadingPageFeature.strings.tryOnLoadingStatusGeneratingOutfit
                }

                is ProductGenerationOperation.LoadingOperation.UploadedSourceImageOperation -> {
                    loadingPageFeature.strings.tryOnLoadingStatusScanningBody
                }

                is ProductGenerationOperation.LoadingOperation.StartGenerationOperation -> {
                    loadingPageFeature.strings.tryOnLoadingStatusUploadingImage
                }

                else -> {
                    loadingPageFeature.strings.tryOnLoadingStatusScanningBody
                }
            }
    }

    return loadingText
}
