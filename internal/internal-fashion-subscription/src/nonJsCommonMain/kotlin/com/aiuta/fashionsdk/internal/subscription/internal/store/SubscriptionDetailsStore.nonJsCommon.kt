package com.aiuta.fashionsdk.internal.subscription.internal.store

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage

private const val STORAGE_NAME = "aiuta_subscription_details"
private const val KEY_PREFIX = "details_"

internal actual fun createSubscriptionDetailsStore(platformContext: AiutaPlatformContext): SubscriptionDetailsStore {
    // Resolved before the lazy initializer, so it never holds the original context
    val storageContext = platformContext.toStorageContext()
    return StorageSubscriptionDetailsStore(
        storage = lazy { AiutaStorage.Factory().create(STORAGE_NAME, storageContext) },
    )
}

internal class StorageSubscriptionDetailsStore(
    private val storage: Lazy<AiutaStorage>,
) : SubscriptionDetailsStore {
    override suspend fun read(subscriptionId: String): StoredSubscriptionDetails? = storage.value.get(
        key = KEY_PREFIX + subscriptionId,
        serializer = StoredSubscriptionDetails.serializer(),
    )

    override suspend fun write(
        subscriptionId: String,
        details: StoredSubscriptionDetails,
    ) {
        storage.value.save(
            key = KEY_PREFIX + subscriptionId,
            value = details,
            serializer = StoredSubscriptionDetails.serializer(),
        )
    }
}
