package com.aiuta.fashionsdk.tryon.compose.data.internal.repository.config

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.config.ConfigLocalDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.config.ConfigRemoteDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.base.BaseRepository
import com.aiuta.fashionsdk.tryon.compose.domain.internal.time.TimeSaver
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.ClientConfig
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features.PoweredByStickerFeature
import java.util.concurrent.TimeUnit

internal class ConfigRepository(
    private val timeSaver: TimeSaver,
    private val localDataSource: ConfigLocalDataSource,
    private val remoteDataSource: ConfigRemoteDataSource,
) : BaseRepository(REPOSITORY_KEY, timeSaver) {
    suspend fun loadConfig(forceUpdate: Boolean = false): ClientConfig {
        return updatableLoad(
            delayMilliseconds = CONFIG_UPDATE_DURATION,
            forceUpdate = forceUpdate,
            remoteLoad = {
                val etag = localDataSource.getEtag()
                remoteDataSource.getBackendConfig(etag)
            },
            localLoad = {
                localDataSource.getBackendConfig()
            },
            replaceLocalData = { config ->
                localDataSource.replaceConfig(config)
            },
        )
    }

    suspend fun getPoweredByStickerFeature(forceUpdate: Boolean = false): PoweredByStickerFeature? {
        return loadConfig(forceUpdate).clientConfiguration.poweredByStickerFeature
    }

    companion object {
        private const val REPOSITORY_KEY = "ConfigRepository"
        private val CONFIG_UPDATE_DURATION = TimeUnit.MINUTES.toMillis(30)

        fun getInstance(aiuta: Aiuta): ConfigRepository {
            return ConfigRepository(
                timeSaver = TimeSaver.getInstance(aiuta.application),
                localDataSource = ConfigLocalDataSource.getInstance(aiuta.application),
                remoteDataSource = ConfigRemoteDataSource.getInstance(aiuta),
            )
        }
    }
}
