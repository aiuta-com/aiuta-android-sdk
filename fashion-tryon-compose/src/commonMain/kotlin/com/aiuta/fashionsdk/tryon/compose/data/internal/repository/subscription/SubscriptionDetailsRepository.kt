package com.aiuta.fashionsdk.tryon.compose.data.internal.repository.subscription

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription.SubscriptionDetailsLocalDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription.SubscriptionDetailsRemoteDataSource
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.SubscriptionDetailsConfig
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.features.PoweredByStickerFeature
import com.aiuta.fashionsdk.tryon.compose.data.internal.repository.base.BaseRepository
import com.aiuta.fashionsdk.tryon.compose.domain.internal.time.TimeSaver
import kotlin.time.Duration.Companion.minutes

internal class SubscriptionDetailsRepository(
    private val timeSaver: TimeSaver,
    private val localDataSource: SubscriptionDetailsLocalDataSource,
    private val remoteDataSource: SubscriptionDetailsRemoteDataSource,
    private val logger: AiutaLogger?,
) : BaseRepository(REPOSITORY_KEY, timeSaver) {
    suspend fun loadConfig(forceUpdate: Boolean = false): SubscriptionDetailsConfig = updatableLoad(
        delay = CONFIG_UPDATE_DURATION,
        forceUpdate = forceUpdate,
        remoteLoad = { forceLoad ->
            val etag = localDataSource.getEtag()

            remoteDataSource.getSubscriptionDetails(etag.takeIf { !forceLoad }).also {
                logger?.d("SubscriptionDetailsRepository: remote load with etag($etag) - $it")
            }
        },
        localLoad = {
            localDataSource.getSubscriptionDetailsConfig().also {
                logger?.d("SubscriptionDetailsRepository: local load - $it")
            }
        },
        replaceLocalData = { config ->
            logger?.d("SubscriptionDetailsRepository: update subscription details config, new config - $config")
            localDataSource.replaceConfig(config)
        },
    )

    suspend fun getPowerByStickerFeature(forceUpdate: Boolean = false): PoweredByStickerFeature? = loadConfig(forceUpdate).details?.poweredByStickerFeature

    companion object {
        private const val REPOSITORY_KEY = "SubscriptionDetailsRepository"
        private val CONFIG_UPDATE_DURATION = 30.minutes

        fun getInstance(aiuta: Aiuta): SubscriptionDetailsRepository = SubscriptionDetailsRepository(
            timeSaver = TimeSaver.getInstance(aiuta.platformContext),
            localDataSource = SubscriptionDetailsLocalDataSource.getInstance(aiuta.platformContext),
            remoteDataSource = SubscriptionDetailsRemoteDataSource.getInstance(aiuta),
            logger = aiuta.logger,
        )
    }
}
