package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarController
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.warmup.WarmUpInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.utils.asCustom
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toImageUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.toOperationUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.DeleteGeneratedImagesToastErrorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.DeleteUploadedImagesToastErrorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.updateActiveOperationOrSetEmpty
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

// Generated images
@Composable
internal fun AiutaTryOnLoadingActionsController.deletingGeneratedImagesListener() {
    updateDeletingGeneratedImagesListener()
}

@Composable
private fun AiutaTryOnLoadingActionsController.updateDeletingGeneratedImagesListener() {
    val controller = LocalController.current

    val generationsHistoryFeature = provideFeature<AiutaTryOnGenerationsHistoryFeature>()
    val dataProvider = generationsHistoryFeature?.dataProvider?.asCustom()

    // Observe external changes of generated images and delete
    dataProvider?.let {
        LaunchedEffect(Unit) {
            dataProvider
                .generatedImages
                .map { images ->
                    // Make as list to compensate forEach with inner contains
                    images.map { image -> image.toImageUiModel() }.toSet()
                }
                .onEach { images ->
                    // Get current loadings & retries
                    val loadingActiveGenerations = loadingGenerationsHolder.getList()
                    val retryActiveGenerations = retryGenerationsHolder.getList()
                    val sessionGenerations =
                        controller.sessionGenerationInteractor.sessionGenerations.toList()

                    val imagesIds = images.map { it.id }.toSet()

                    // Clean retries, because they can executed a little bit later
                    retryActiveGenerations.forEach { activeGeneration ->
                        if (!imagesIds.contains(activeGeneration.id)) {
                            retryGenerationsHolder.remove(activeGeneration)
                        }
                    }

                    // Clean loadings
                    loadingActiveGenerations.forEach { activeGeneration ->
                        if (!imagesIds.contains(activeGeneration.id)) {
                            loadingGenerationsHolder.remove(activeGeneration)
                        }
                    }

                    // Clean session history
                    sessionGenerations.forEach { generation ->
                        if (!imagesIds.contains(generation.id)) {
                            controller.sessionGenerationInteractor.deleteGeneration(generation)
                        }
                    }
                }
                .launchIn(generalScope)
        }
    }
}

internal fun Result<Unit>.listenErrorDeletingGeneratedImages(
    controller: FashionTryOnController,
    errorSnackbarController: AiutaErrorSnackbarController,
    loadingActionsController: AiutaTryOnLoadingActionsController,
): Result<Unit> = onFailure {
    // Move from loading to retry
    val retryGenerations = loadingActionsController.loadingGenerationsHolder.getList()
    loadingActionsController.retryGenerationsHolder.putAll(retryGenerations)

    // Clean loading
    loadingActionsController.loadingGenerationsHolder.removeAll()

    errorSnackbarController.showErrorState(
        newErrorState = DeleteGeneratedImagesToastErrorState(
            controller = controller,
            errorSnackbarController = errorSnackbarController,
            loadingActionsController = loadingActionsController,
        ),
    )
}

// Uploaded images
@Composable
internal fun AiutaTryOnLoadingActionsController.deletingUploadedImagesListener(
    controller: FashionTryOnController,
) {
    updateDeletingUploadedImagesListener(controller)
}

@Composable
private fun AiutaTryOnLoadingActionsController.updateDeletingUploadedImagesListener(
    controller: FashionTryOnController,
) {
    val uploadsHistoryFeature = provideFeature<AiutaImagePickerUploadsHistoryFeature>()
    val customDataProvider = uploadsHistoryFeature?.dataProvider?.asCustom()

    // Observe external changes of generated images and delete
    customDataProvider?.let {
        val context = LocalPlatformContext.current
        val warmUpInteractor = remember { WarmUpInteractor(context) }

        LaunchedEffect(Unit) {
            customDataProvider
                .uploadedImages
                .map { operations ->
                    // Make as list to compensate forEach with inner contains
                    operations.map { operation -> operation.toOperationUiModel() }.toSet()
                }
                .onEach { operations ->
                    // Get current loadings & retries
                    val loadingActiveUploads = loadingUploadsHolder.getList()
                    val retryActiveUploads = retryUploadsHolder.getList()

                    val operationsIds = operations.map { it.operationId }.toSet()

                    // Clean retries, because they can executed a little bit later
                    retryActiveUploads.forEach { activeUpload ->
                        if (!operationsIds.contains(activeUpload.operationId)) {
                            retryUploadsHolder.remove(activeUpload)
                        }
                    }

                    // Delete active loading uploads
                    loadingActiveUploads.forEach { activeUpload ->
                        if (!operationsIds.contains(activeUpload.operationId)) {
                            loadingUploadsHolder.remove(activeUpload)
                        }
                    }

                    // Warm up image
                    val operation = operations.firstOrNull()
                    val imageUrl = operation?.sourceImageUrls?.firstOrNull()

                    imageUrl?.let { warmUpInteractor.saveWarmUp(it) }

                    // Save delay for smooth changing
                    delay(700)

                    // Update active operation
                    controller.updateActiveOperationOrSetEmpty(operation)
                }
                .launchIn(generalScope)
        }
    }
}

internal fun Result<Unit>.listenErrorDeletingUploadedImages(
    controller: FashionTryOnController,
    errorSnackbarController: AiutaErrorSnackbarController,
    loadingActionsController: AiutaTryOnLoadingActionsController,
): Result<Unit> = onFailure {
    // Move from loading to retry
    val retryOperations = loadingActionsController.loadingUploadsHolder.getList()
    loadingActionsController.retryUploadsHolder.putAll(retryOperations)

    // Clean loading
    loadingActionsController.loadingUploadsHolder.removeAll()

    errorSnackbarController.showErrorState(
        newErrorState = DeleteUploadedImagesToastErrorState(
            controller = controller,
            errorSnackbarController = errorSnackbarController,
            loadingActionsController = loadingActionsController,
        ),
    )
}
