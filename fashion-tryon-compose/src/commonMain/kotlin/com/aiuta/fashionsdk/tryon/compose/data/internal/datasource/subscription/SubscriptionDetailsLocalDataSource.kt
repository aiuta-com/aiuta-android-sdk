package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.AppDatabase
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription.dao.SubscriptionDetailsDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription.dao.replaceAll
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.subscription.toDTO
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.subscription.toEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.SubscriptionDetailsConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class SubscriptionDetailsLocalDataSource(
    private val subscriptionDetailsDao: SubscriptionDetailsDao,
) {
    suspend fun getEtag(): String? = withContext(Dispatchers.IO) {
        subscriptionDetailsDao.getAllEtags(limit = 1).firstOrNull()
    }

    suspend fun getSubscriptionDetailsConfig(): SubscriptionDetailsConfig? = withContext(Dispatchers.IO) {
        subscriptionDetailsDao.getAll(limit = 1).firstOrNull()?.toDTO()
    }

    suspend fun replaceConfig(newConfig: SubscriptionDetailsConfig) = withContext(Dispatchers.IO) {
        subscriptionDetailsDao.replaceAll(newConfig.toEntity())
    }

    companion object Companion {
        fun getInstance(platformContext: AiutaPlatformContext): SubscriptionDetailsLocalDataSource = SubscriptionDetailsLocalDataSource(
            subscriptionDetailsDao = AppDatabase.getInstance(platformContext).subscriptionDetailsDao(),
        )
    }
}
