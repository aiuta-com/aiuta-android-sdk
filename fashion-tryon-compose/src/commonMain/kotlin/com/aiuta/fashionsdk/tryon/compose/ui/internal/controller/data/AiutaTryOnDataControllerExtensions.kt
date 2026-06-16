package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data

import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.toUiModel

internal suspend fun AiutaTryOnDataController.preloadConfig() {
    try {
        tryOnModelsRepository.loadTryOnModels()
        subscriptionDetailsRepository.loadConfig()
    } catch (e: Exception) {
        // Failed to preload config
    }
}

internal suspend fun AiutaTryOnDataController.provideTryOnModels(
    forceUpdate: Boolean = false,
): Result<List<TryOnModelUiModel>> = runCatching {
    tryOnModelsRepository
        .loadTryOnModels(forceUpdate)
        .map { model -> model.toUiModel() }
}

internal suspend fun AiutaTryOnDataController.providePowerBySticker(
    forceUpdate: Boolean = false,
): Result<PoweredByStickerFeature?> = runCatching {
    subscriptionDetailsRepository.getPowerByStickerFeature(forceUpdate)
}
