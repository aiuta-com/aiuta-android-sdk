package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller

import androidx.compose.runtime.Immutable
import coil3.PlatformContext
import com.aiuta.fashionsdk.configuration.features.AiutaFeatures
import com.aiuta.fashionsdk.configuration.features.tryon.validation.strings.AiutaTryOnInputImageValidationFeatureStrings
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.dialog.AiutaTryOnDialogController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.AiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.listenErrorDeletingGeneratedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.listenErrorDeletingUploadedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.utils.startGeneration
import kotlinx.coroutines.launch

@Immutable
@Deprecated("Migrate new nav module")
internal interface ToastErrorState {
    val message: String?
    val onRetry: () -> Unit
    val onClose: (() -> Unit)?
}

@Immutable
internal class TryOnToastErrorState(
    coilContext: PlatformContext,
    controller: FashionTryOnController,
    dialogController: AiutaTryOnDialogController,
    features: AiutaFeatures,
    inputImageValidationStrings: AiutaTryOnInputImageValidationFeatureStrings,
) : ToastErrorState {
    override val message: String? = null
    override val onRetry: () -> Unit = {
        controller.startGeneration(
            dialogController = dialogController,
            coilContext = coilContext,
            features = features,
            inputImageValidationStrings = inputImageValidationStrings,
        )
    }
    override val onClose: (() -> Unit)? = null
}

@Immutable
internal class DeleteGeneratedImagesToastErrorState(
    controller: FashionTryOnController,
    loadingActionsController: AiutaTryOnLoadingActionsController,
) : ToastErrorState {
    override val message: String? = null
    override val onRetry: () -> Unit = {
        with(loadingActionsController) {
            generalScope.launch {
                // Get retries generations
                val retryGenerations = retryGenerationsHolder.getList()
                // Clean retries and mark them as loading
                retryGenerationsHolder.removeAll()
                loadingGenerationsHolder.putAll(retryGenerations)

                // Execute retry
                controller
                    .generatedImageInteractor
                    .remove(retryGenerations)
                    .listenErrorDeletingGeneratedImages(
                        controller = controller,
                        loadingActionsController = loadingActionsController,
                    )
            }
        }
    }
    override val onClose: (() -> Unit) = {
        // Clean retries
        loadingActionsController.retryGenerationsHolder.removeAll()
    }
}

@Immutable
internal class DeleteUploadedImagesToastErrorState(
    controller: FashionTryOnController,
    loadingActionsController: AiutaTryOnLoadingActionsController,
) : ToastErrorState {
    override val message: String? = null
    override val onRetry: () -> Unit = {
        with(loadingActionsController) {
            generalScope.launch {
                // Get retries uploads
                val retryOperations = retryUploadsHolder.getList()
                // Clean retries and mark them as loading
                retryUploadsHolder.removeAll()
                loadingUploadsHolder.putAll(retryOperations)

                // Execute retry
                controller.generatedOperationInteractor
                    .deleteOperations(retryOperations)
                    .listenErrorDeletingUploadedImages(
                        controller = controller,
                        loadingActionsController = loadingActionsController,
                    )
            }
        }
    }
    override val onClose: (() -> Unit) = {
        // Clean retries
        loadingActionsController.retryUploadsHolder.removeAll()
    }
}
