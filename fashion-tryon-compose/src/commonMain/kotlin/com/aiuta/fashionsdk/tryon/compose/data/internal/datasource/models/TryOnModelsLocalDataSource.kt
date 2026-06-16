package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.models

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.buildAiutaTryOnStorage
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models.CachedTryOnModels

internal class TryOnModelsLocalDataSource(
    private val storage: AiutaStorage,
) {
    suspend fun getCachedModels(): CachedTryOnModels? = storage.get(
        key = TRY_ON_MODELS_KEY,
        serializer = CachedTryOnModels.serializer(),
    )

    suspend fun saveCachedModels(value: CachedTryOnModels) {
        storage.save(
            key = TRY_ON_MODELS_KEY,
            value = value,
            serializer = CachedTryOnModels.serializer(),
        )
    }

    companion object {
        private const val TRY_ON_MODELS_KEY = "try_on_models"

        fun getInstance(platformContext: AiutaPlatformContext): TryOnModelsLocalDataSource = TryOnModelsLocalDataSource(
            storage = buildAiutaTryOnStorage(platformContext),
        )
    }
}
