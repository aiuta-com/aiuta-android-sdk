package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils

import coil3.PlatformContext
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.tryon.validation.strings.AiutaTryOnInputImageValidationFeatureStrings
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations.GeneratedOperationFactory
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations.isEmptyInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.warmup.WarmUpInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.UrlImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.size
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.GeneratedOperationUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationOperation
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.toOperation
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendErrorDownloadResultEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendSuccessTryOnEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.TryOnToastErrorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateGeneration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateGeneration
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.AiutaTryOnDialogController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.AiutaTryOnDialogState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.hideDialog
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.showDialog
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.showErrorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationWithFirstOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationPlatformImageContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationUrlContainer
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.isTryOnGenerationAbortedException
import kotlin.time.measureTimedValue
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal fun FashionTryOnController.startGeneration(
    coilContext: PlatformContext,
    dialogController: AiutaTryOnDialogController,
    features: AiutaFeatures,
    inputImageValidationStrings: AiutaTryOnInputImageValidationFeatureStrings,
) {
    generationScope.launch {
        activateGeneration()

        val errorCount = atomic(0)
        val generatedOperationFactory = GeneratedOperationFactory(generatedOperationInteractor)

        val rawGenerationFlows =
            solveStartGenerationFlows(
                generatedOperationFactory = generatedOperationFactory,
            )
        val generationFlows =
            rawGenerationFlows.mapIndexed { index, generationFlow ->
                generationFlow
                    .mapNotNull { status -> status.toOperation() }
                    .catch {
                        if (errorCount.incrementAndGet() == lastSavedImages.value.size) {
                            generationStatus.value = ProductGenerationUIStatus.IDLE
                            deactivateGeneration()
                        }
                    }
            }

        generationFlows
            .merge()
            .cancellable()
            .collect { operation ->
                solveOperationCollecting(
                    coilContext = coilContext,
                    dialogController = dialogController,
                    features = features,
                    operation = operation,
                    inputImageValidationStrings = inputImageValidationStrings,
                    generatedOperationFactory = generatedOperationFactory,
                )
            }
    }
}

private fun FashionTryOnController.solveStartGenerationFlows(
    generatedOperationFactory: GeneratedOperationFactory,
): List<Flow<ProductGenerationStatus>> = when (val activeImages = lastSavedImages.value) {
    is LastSavedImages.PlatformImageSource -> {
        startGenerationWithUriSource(
            platformImageSource = activeImages,
            generatedOperationFactory = generatedOperationFactory,
        )
    }

    is LastSavedImages.UrlSource -> {
        startGenerationWithUrlSource(
            urlSource = activeImages,
            generatedOperationFactory = generatedOperationFactory,
        )
    }

    is LastSavedImages.Empty -> emptyList()
}

private fun FashionTryOnController.startGenerationWithUriSource(
    platformImageSource: LastSavedImages.PlatformImageSource,
    generatedOperationFactory: GeneratedOperationFactory,
): List<Flow<ProductGenerationStatus>> = platformImageSource.platformFiles.map { file ->
    aiutaTryOn
        .startProductGeneration(
            container = ProductGenerationPlatformImageContainer(
                platformFile = file,
                productId = activeProductItem.value.id,
            ),
        )
        .onEach { status ->
            // Notify (or create locally) about new operation, after success
            // Also need update active operation, if we change local URI to backend URL
            if (status is ProductGenerationStatus.SuccessGenerationStatus) {
                val currentOperationId = generatedOperationFactory.getOperationId(
                    imageId = status.sourceImageId,
                )

                generatedOperationInteractor.createImage(
                    sourceImageId = status.sourceImageId,
                    sourceImageUrl = status.sourceImageUrl,
                    sourceImageType = status.sourceImageType,
                    operationId = currentOperationId,
                )
            }
        }
}

private fun FashionTryOnController.startGenerationWithUrlSource(
    urlSource: LastSavedImages.UrlSource,
    generatedOperationFactory: GeneratedOperationFactory,
): List<Flow<ProductGenerationStatus>> = urlSource.urlImages.map { sourceImage ->
    aiutaTryOn
        .startProductGeneration(
            container = ProductGenerationUrlContainer(
                fileId = sourceImage.imageId,
                fileUrl = sourceImage.imageUrl,
                fileType = sourceImage.imageType,
                productId = activeProductItem.value.id,
            ),
        )
        .onEach { status ->
            // Notify (or create locally) about new operation, after success
            // Also need update active operation, if we change local URI to backend URL
            val isModelSource =
                lastSavedImages.value is LastSavedImages.UrlSource.PregeneratedModels

            if (isModelSource && status is ProductGenerationStatus.SuccessGenerationStatus) {
                val currentOperationId = generatedOperationFactory.getOperationId(
                    imageId = status.sourceImageId,
                )

                generatedOperationInteractor.createImage(
                    sourceImageId = status.sourceImageId,
                    sourceImageUrl = status.sourceImageUrl,
                    sourceImageType = status.sourceImageType,
                    operationId = currentOperationId,
                )
            }
        }
}

// Collecting
private suspend fun FashionTryOnController.solveOperationCollecting(
    coilContext: PlatformContext,
    dialogController: AiutaTryOnDialogController,
    features: AiutaFeatures,
    operation: ProductGenerationOperation,
    inputImageValidationStrings: AiutaTryOnInputImageValidationFeatureStrings,
    generatedOperationFactory: GeneratedOperationFactory,
) {
    when (operation) {
        is ProductGenerationOperation.LoadingOperation -> {
            if (generationStatus.value != ProductGenerationUIStatus.SUCCESS) {
                generationStatus.value = ProductGenerationUIStatus.LOADING
            }

            // Check is this operation already exist
            // Should think about optimization for O(n)
            val existedOperation =
                generationOperations.find { it.operationId == operation.operationId }
            val existedOperationIndex =
                existedOperation?.let {
                    generationOperations.indexOf(
                        existedOperation,
                    )
                }

            if (existedOperationIndex == null) {
                // Add as last
                generationOperations.add(operation)
            } else {
                // Just update existed
                generationOperations[existedOperationIndex] = operation
            }
        }

        is ProductGenerationOperation.SuccessOperation -> {
            generalScope.launch {
                // Try warm up first
                val isSuccessfullyUpload = addSuccessGenerations(operation)

                if (isSuccessfullyUpload) {
                    // Success case
                    // Let's save to history
                    saveGenerations(features = features, operation = operation)

                    // Set state as success
                    generationStatus.value = ProductGenerationUIStatus.SUCCESS
                    refreshOperation(operation)

                    // Navigate to results
                    navigateTo(NavigationScreen.GenerationResult)
                    deactivateGeneration()

                    // Only after navigation, let's change if need active image to backend
                    refreshActiveImage(
                        coilContext = coilContext,
                        generatedOperationFactory = generatedOperationFactory,
                        operation = operation,
                    )
                } else {
                    // Negative case
                    // Deactivate generation
                    deactivateGeneration()
                    generationStatus.value = ProductGenerationUIStatus.IDLE

                    showErrorState(
                        errorState = TryOnToastErrorState(
                            controller = this@solveOperationCollecting,
                            dialogController = dialogController,
                            coilContext = coilContext,
                            features = features,
                            inputImageValidationStrings = inputImageValidationStrings,
                        ),
                    )
                }
            }
        }

        is ProductGenerationOperation.ErrorOperation -> {
            // Deactivate generation
            deactivateGeneration()
            generationStatus.value = ProductGenerationUIStatus.IDLE

            when {
                operation.exception?.isTryOnGenerationAbortedException() == true -> {
                    // Change to last success or empty operation
                    updateActiveOperationWithFirstOrSetEmpty()

                    // Need to change photo from user
                    dialogController.showDialog(
                        dialogState =
                        AiutaTryOnDialogState(
                            description = inputImageValidationStrings.invalidInputImageDescription,
                            confirmButton = inputImageValidationStrings.invalidInputImageChangePhotoButton,
                            onConfirm = dialogController::hideDialog,
                        ),
                    )
                }

                else -> {
                    showErrorState(
                        errorState =
                        TryOnToastErrorState(
                            controller = this@solveOperationCollecting,
                            coilContext = coilContext,
                            dialogController = dialogController,
                            features = features,
                            inputImageValidationStrings = inputImageValidationStrings,
                        ),
                    )
                }
            }

            refreshOperation(operation)
        }
    }
}

private suspend fun FashionTryOnController.refreshActiveImage(
    coilContext: PlatformContext,
    generatedOperationFactory: GeneratedOperationFactory,
    operation: ProductGenerationOperation.SuccessOperation,
) {
    when {
        // Feature of operation is not initialized - skip saving operation
        generatedOperationInteractor.isEmptyInteractor() -> {
            updateActiveOperationOrSetEmpty(null)
        }

        // Refresh only if active operation with local image
        lastSavedImages.value is LastSavedImages.PlatformImageSource -> {
            val warmUpInteractor = WarmUpInteractor(coilContext)

            val currentOperationId = generatedOperationFactory.getOperationId(
                imageId = operation.uploadedSourceImageId,
            )
            if (lastSavedOperation.value?.operationId != currentOperationId) {
                val images =
                    operation.generatedImages.map { image ->
                        UrlImage(
                            imageId = image.id,
                            imageUrl = image.imageUrl,
                            imageType = image.type,
                        )
                    }

                // Warm up for smooth change
                images.firstOrNull()?.let { image ->
                    warmUpInteractor.saveWarmUp(image.imageUrl)
                }

                // Change to new
                val newOperation = GeneratedOperationUIModel(
                    operationId = currentOperationId,
                    urlImages = images,
                )

                updateActiveOperationOrSetEmpty(newOperation)
            }
        }
    }
}

private fun FashionTryOnController.refreshOperation(newOperation: ProductGenerationOperation) {
    generationOperations.forEachIndexed { index, oldOperation ->
        if (oldOperation.operationId == newOperation.operationId) {
            generationOperations[index] = newOperation
        }
    }
}

/**
 * @return true, if successfully load images and send analytic
 */
private suspend fun FashionTryOnController.addSuccessGenerations(
    newOperation: ProductGenerationOperation.SuccessOperation,
): Boolean = try {
    val result = measureTimedValue {
        sessionGenerationInteractor.addGenerations(newOperation.generatedImages)
    }

    sendSuccessTryOnEvent(
        metadata = newOperation.metadata,
        downloadDuration = result.duration,
    )

    true
} catch (e: Exception) {
    sendErrorDownloadResultEvent()
    false
}

private suspend fun FashionTryOnController.saveGenerations(
    features: AiutaFeatures,
    operation: ProductGenerationOperation.SuccessOperation,
) {
    // Save generations for history, if operation is success and history available
    if (features.isFeatureInitialize<AiutaTryOnGenerationsHistoryFeature>()) {
        generatedImageInteractor.insertAll(
            generatedProductId = activeProductItem.value.id,
            images = operation.generatedImages,
        )
    }
}
