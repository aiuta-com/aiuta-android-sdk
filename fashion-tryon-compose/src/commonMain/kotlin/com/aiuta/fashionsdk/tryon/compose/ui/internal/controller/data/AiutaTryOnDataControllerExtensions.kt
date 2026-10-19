package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data

import com.aiuta.fashionsdk.internal.subscription.model.AiutaPoweredBySticker
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toUiModel
import kotlin.time.Duration.Companion.seconds

internal suspend fun AiutaTryOnDataController.preloadConfig() {
    // Never throws; also gets the polling delays ready long before the first
    // generation
    subscription.preload()

    try {
        tryOnModelsRepository.loadTryOnModels()
    } catch (e: Exception) {
        // Failed to preload config
    }
}

internal suspend fun AiutaTryOnDataController.provideTryOnModels(
    forceUpdate: Boolean = false,
): Result<List<TryOnModelUiModel>> = runCatching {
    tryOnModelsRepository
        .loadTryOnModels(forceUpdate)
        .mapNotNull { model -> model.toUiModel() }
}

internal suspend fun AiutaTryOnDataController.providePowerBySticker(): Result<AiutaPoweredBySticker?> = runCatching {
    subscription.getDetails(timeout = SUBSCRIPTION_DETAILS_TIMEOUT)?.poweredBySticker
}

// Waited only when no details are cached yet: they're preloaded on the splash
private val SUBSCRIPTION_DETAILS_TIMEOUT = 10.seconds
