package com.aiuta.fashionsdk.internal.subscription.internal.store

import com.aiuta.fashionsdk.context.AiutaPlatformContext

// DataStore doesn't support JS/Wasm: the details are kept in memory only
internal actual fun createSubscriptionDetailsStore(platformContext: AiutaPlatformContext): SubscriptionDetailsStore = object : SubscriptionDetailsStore {
    override suspend fun read(subscriptionId: String): StoredSubscriptionDetails? = null

    override suspend fun write(
        subscriptionId: String,
        details: StoredSubscriptionDetails,
    ) = Unit
}
