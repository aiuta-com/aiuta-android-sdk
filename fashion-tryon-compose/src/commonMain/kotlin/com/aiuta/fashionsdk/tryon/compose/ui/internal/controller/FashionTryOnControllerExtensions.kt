package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.compose.uikit.utils.isFeatureInitialize
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.isNotEmpty
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toLastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.GeneratedOperationUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationOperation
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode

// Edit changePhotoButtonStyle
internal fun FashionTryOnController.activateSelectMode() {
    selectorState.value = SelectorMode.ALL_IS_NOT_SELECTED
}

internal fun FashionTryOnController.deactivateSelectMode() {
    selectorState.value = SelectorMode.DISABLED
    selectorHolder.removeAll()
}

// Generation helpers
internal fun FashionTryOnController.activateGeneration() {
    generationStatus.value = ProductGenerationUIStatus.LOADING
    isGenerationActive.value = true
}

internal fun FashionTryOnController.deactivateGeneration() {
    isGenerationActive.value = false
}

// Generations
internal inline fun <reified T : ProductGenerationOperation> FashionTryOnController.tryToGetOperations(): List<T> = generationOperations.mapNotNull { it as? T }

@Composable
internal fun FashionTryOnController.subscribeToSuccessOperations(): State<List<ProductGenerationOperation.SuccessOperation>> = remember(generationOperations) {
    derivedStateOf { tryToGetOperations() }
}

@Composable
internal fun FashionTryOnController.subscribeToLoadingOperations(): State<List<ProductGenerationOperation.LoadingOperation>> = remember(generationOperations) {
    derivedStateOf { tryToGetOperations() }
}

// Auto try on changePhotoButtonStyle
internal fun FashionTryOnController.activateAutoTryOn() {
    isAutoTryOnEnabled.value = true
}

internal fun FashionTryOnController.disableAutoTryOn() {
    isAutoTryOnEnabled.value = false
}

internal fun FashionTryOnController.updateActiveOperationOrSetEmpty(
    operation: GeneratedOperationUIModel?,
) {
    // Try to update with new or set as empty
    if (operation != null) {
        lastSavedOperation.value = operation
        lastSavedImages.value = operation.toLastSavedImages()
    } else {
        lastSavedOperation.value = null
        lastSavedImages.value = LastSavedImages.Empty
    }
}

internal suspend fun FashionTryOnController.updateActiveOperationWithFirstOrSetEmpty() {
    val firstOperation = generatedOperationInteractor.getFirstGeneratedOperation()
    // Try to update with new or set as empty
    if (firstOperation != null) {
        lastSavedOperation.value = firstOperation
        lastSavedImages.value = firstOperation.toLastSavedImages()
    } else {
        lastSavedOperation.value = null
        lastSavedImages.value = LastSavedImages.Empty
    }
}

// Checks
@Composable
internal fun FashionTryOnController.isAppbarHistoryAvailable(): State<Boolean> {
    val historyImageCount = generatedImageInteractor.countFlow().collectAsState(0)
    val isGenerationsHistoryFeatureAvailable =
        isFeatureInitialize<AiutaTryOnGenerationsHistoryFeature>()

    return remember(generationStatus.value) {
        derivedStateOf {
            val isGenerationNotLoading = generationStatus.value != ProductGenerationUIStatus.LOADING
            val isGenerationsHistoryNotEmpty = historyImageCount.value != 0

            isGenerationNotLoading && isGenerationsHistoryFeatureAvailable && isGenerationsHistoryNotEmpty
        }
    }
}

@Composable
internal fun FashionTryOnController.isAppbarSelectAvailable(): State<Boolean> = remember(selectorState.value) {
    derivedStateOf {
        selectorState.value == SelectorMode.DISABLED
    }
}

@Composable
internal fun FashionTryOnController.isSelectModeActive(): State<Boolean> = remember(selectorState.value) {
    derivedStateOf {
        selectorState.value != SelectorMode.DISABLED
    }
}

@Composable
internal fun FashionTryOnController.isLastSavedPhotoAvailable(): State<Boolean> = remember(lastSavedImages.value) {
    derivedStateOf {
        lastSavedImages.value.isNotEmpty()
    }
}

@Composable
internal fun FashionTryOnController.isSingleTryOnMode(): State<Boolean> = remember(activeProductItems) {
    derivedStateOf {
        activeProductItems.size == 1
    }
}
