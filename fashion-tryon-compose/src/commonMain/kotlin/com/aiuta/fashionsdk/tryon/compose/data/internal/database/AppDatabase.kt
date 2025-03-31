package com.aiuta.fashionsdk.tryon.compose.data.internal.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.builder.buildRoomDatabase
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.FeedbackFeatureConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.FitDisclaimerFeatureConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.PoweredByStickerFeatureConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.converters.TryOnModelsCategoriesConverter
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.code.dao.AiutaCodeDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.code.dao.replaceAll
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.config.dao.ConfigDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.generated.images.dao.GeneratedImageDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.generated.operations.dao.GeneratedOperationDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.generated.operations.dao.SourceImageDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.onboarding.dao.OnboardingDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.time.dao.TimeDao
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.code.AiutaCodeEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.config.ClientConfigEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.generated.images.GeneratedImageEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.generated.images.SourceImageEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.generated.operations.GeneratedOperationEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.onboarding.OnboardingEntity
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.local.time.TimestampEntity
import kotlin.concurrent.Volatile
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal const val DATABASE_VERSION = 12
internal const val DATABASE_NAME = "fashionsdk-database"

@Database(
    entities = [
        // Config
        ClientConfigEntity::class,

        // Time
        TimestampEntity::class,

        // Generated images
        GeneratedImageEntity::class,

        // Generation operation
        GeneratedOperationEntity::class,
        SourceImageEntity::class,

        // Onboarding saver
        OnboardingEntity::class,

        // Aiuta code checker
        AiutaCodeEntity::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = false,
)
@TypeConverters(
    value = [
        // Config
        PoweredByStickerFeatureConverter::class,
        FeedbackFeatureConverter::class,
        FitDisclaimerFeatureConverter::class,
        TryOnModelsCategoriesConverter::class,
    ],
)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    // Remote config
    abstract fun configDao(): ConfigDao

    // Remote config
    abstract fun timeDao(): TimeDao

    // Generated images
    abstract fun generatedImageDao(): GeneratedImageDao

    // Generation operation
    abstract fun generatedOperationDao(): GeneratedOperationDao

    abstract fun sourceImageDao(): SourceImageDao

    // Onboarding saver
    abstract fun onboardingDao(): OnboardingDao

    // Aiuta code checker
    abstract fun aiutaCodeDao(): AiutaCodeDao

    companion object : SynchronizedObject() {
        @Volatile
        private var instance: AppDatabase? = null
        private val mutex = Mutex()

        fun getInstance(platformContext: AiutaPlatformContext): AppDatabase {
            synchronized(this) {
                var currentInstance = instance

                if (currentInstance == null) {
                    currentInstance = buildRoomDatabase(platformContext)

                    instance = currentInstance
                }
                return currentInstance
            }
        }

        suspend fun validateCache(aiuta: Aiuta) {
            mutex.withLock {
                withContext(Dispatchers.IO) {
                    val database = getInstance(aiuta.platformContext)
                    val aiutaCodeDao = database.aiutaCodeDao()
                    val cachedSubscriptionId = aiutaCodeDao.getCodes().firstOrNull()?.subscriptionId

                    // Invalidate all records, if we have new Aiuta instance
                    if (cachedSubscriptionId != aiuta.subscriptionId) {
                        if (cachedSubscriptionId != null) {
                            // Delete all records
                            database.generatedOperationDao().removeAll()
                            database.generatedImageDao().removeAll()
                            database.sourceImageDao().removeAll()
                        }

                        // Update code
                        aiutaCodeDao.replaceAll(
                            aiutaCodeEntity =
                                AiutaCodeEntity(
                                    subscriptionId = aiuta.subscriptionId,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
