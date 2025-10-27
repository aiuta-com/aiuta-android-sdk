package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.data

import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.TryOnModelsCategoryUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.toUiModel

internal suspend fun AiutaTryOnDataController.preloadConfig() {
    try {
        configRepository.loadConfig()
        subscriptionDetailsRepository.loadConfig()
    } catch (e: Exception) {
        // Failed to preload config
    }
}

internal suspend fun AiutaTryOnDataController.provideTryOnModelsCategories(
    predefinedModelCategories: Map<String, String>,
    forceUpdate: Boolean = false,
): Result<List<TryOnModelsCategoryUiModel>?> = kotlin.runCatching {
    configRepository
        .getTryOnModelsCategories(forceUpdate)
        ?.mapNotNull { category ->
            if (category.models.isNotEmpty()) {
                category.toUiModel(predefinedModelCategories)
            } else {
                // Let's skip empty category
                null
            }
        }
}

internal suspend fun AiutaTryOnDataController.providePowerBySticker(
    forceUpdate: Boolean = false,
): Result<PoweredByStickerFeature?> = kotlin.runCatching {
    subscriptionDetailsRepository.getPowerByStickerFeature(forceUpdate)
}
