package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils

import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarController
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images.cleanLoadingGenerations
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.DeleteGeneratedImagesToastErrorState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.AiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.loading.listenErrorDeletingGeneratedImages
import kotlinx.coroutines.launch

internal fun FashionTryOnController.deleteGeneratedImages(
    errorSnackbarController: AiutaErrorSnackbarController,
    loadingActionsController: AiutaTryOnLoadingActionsController,
) {
    generalScope.launch {
        try {
            val images = selectorHolder.getList()

            // After getting list, let's deactivate select mode
            deactivateSelectMode()

            // Show as loading
            loadingActionsController.loadingGenerationsHolder.putAll(images)

            // Delete in db
            generatedImageInteractor
                .remove(images)
                .listenErrorDeletingGeneratedImages(
                    controller = this@deleteGeneratedImages,
                    errorSnackbarController = errorSnackbarController,
                    loadingActionsController = loadingActionsController,
                )
            // Clean, if it local mode
            generatedImageInteractor.cleanLoadingGenerations(
                cleanAction = {
                    loadingActionsController.loadingGenerationsHolder.remove(images)
                },
            )

            // Also delete in session
            sessionGenerationInteractor.deleteGenerations(images)
        } catch (e: Exception) {
            errorSnackbarController.showErrorState(
                newErrorState = DeleteGeneratedImagesToastErrorState(
                    controller = this@deleteGeneratedImages,
                    errorSnackbarController = errorSnackbarController,
                    loadingActionsController = loadingActionsController,
                ),
            )
        }
    }
}
