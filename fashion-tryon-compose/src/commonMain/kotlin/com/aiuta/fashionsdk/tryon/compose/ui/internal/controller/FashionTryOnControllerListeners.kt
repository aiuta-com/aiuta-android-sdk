package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendTerminateEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen

@Composable
internal fun FashionTryOnController.generationNavigationListener() {
    val navigationController = LocalAiutaNavigationController.current

    // We should clear generation status and list of operation
    // every time, when move back from result to selector screen,
    // therefore let's clear it on each navigation to TryOnScreen.ImageSelector
    LaunchedEffect(navigationController.currentScreen.value) {
        if (navigationController.currentScreen.value == TryOnScreen.ImageSelector) {
            if (!isGenerationActive.value) {
                generationStatus.value = ProductGenerationUIStatus.IDLE
            }
            generationOperations.clear()
        }
    }
}

@Composable
internal fun FashionTryOnController.historyAvailabilityListener(
    isGenerationsHistoryFeatureAvailable: Boolean,
) {
    // We should delete all generations, if history not available
    LaunchedEffect(Unit) {
        if (!isGenerationsHistoryFeatureAvailable) {
            generatedImageInteractor.removeAll()
        }
    }
}

@Composable
internal fun FashionTryOnController.generationCancellationListener(
    tryOnFeature: AiutaTryOnFeature,
) {
    if (!tryOnFeature.toggles.isBackgroundExecutionAllowed) {
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer =
                LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        // Notify analytic about termination
                        if (generationStatus.value == ProductGenerationUIStatus.LOADING) {
                            sendTerminateEvent()
                        }

                        // Let's clean all generation processes
                        clean()
                    }
                }

            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the Composition, remove the observer
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}
