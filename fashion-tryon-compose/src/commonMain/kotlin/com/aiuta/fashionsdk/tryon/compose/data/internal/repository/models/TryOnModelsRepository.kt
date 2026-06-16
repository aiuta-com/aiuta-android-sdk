package com.aiuta.fashionsdk.tryon.compose.data.internal.repository.models

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.models.TryOnModelsLocalDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.models.TryOnModelsRemoteDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models.CachedTryOnModels
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models.TryOnModelDTO
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.base.BaseRepository
import com.aiuta.fashionsdk.tryon.compose.domain.internal.time.TimeSaver
import kotlin.time.Duration.Companion.minutes

internal class TryOnModelsRepository(
    private val timeSaver: TimeSaver,
    private val localDataSource: TryOnModelsLocalDataSource,
    private val remoteDataSource: TryOnModelsRemoteDataSource,
) : BaseRepository(REPOSITORY_KEY, timeSaver) {

    suspend fun loadTryOnModels(forceUpdate: Boolean = false): List<TryOnModelDTO> = updatableLoad(
        delay = MODELS_UPDATE_DURATION,
        forceUpdate = forceUpdate,
        remoteLoad = { forceLoad ->
            val cached = localDataSource.getCachedModels()
            val etag = cached?.etag?.takeIf { !forceLoad }

            remoteDataSource.getTryOnModels(etag) ?: cached ?: CachedTryOnModels()
        },
        localLoad = {
            localDataSource.getCachedModels()
        },
        replaceLocalData = { value ->
            localDataSource.saveCachedModels(value)
        },
    ).models

    companion object {
        private const val REPOSITORY_KEY = "TryOnModelsRepository"
        private val MODELS_UPDATE_DURATION = 30.minutes

        fun getInstance(aiuta: Aiuta): TryOnModelsRepository = TryOnModelsRepository(
            timeSaver = TimeSaver.getInstance(aiuta.platformContext),
            localDataSource = TryOnModelsLocalDataSource.getInstance(aiuta.platformContext),
            remoteDataSource = TryOnModelsRemoteDataSource.getInstance(aiuta),
        )
    }
}
