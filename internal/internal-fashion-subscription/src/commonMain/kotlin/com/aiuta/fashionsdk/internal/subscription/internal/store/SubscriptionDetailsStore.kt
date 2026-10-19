package com.aiuta.fashionsdk.internal.subscription.internal.store

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StoredSubscriptionDetails(
    @SerialName("etag")
    val etag: String? = null,
    @SerialName("details")
    val details: AiutaSubscriptionDetails,
)

/**
 * Persists the subscription details between launches
 */
internal interface SubscriptionDetailsStore {
    suspend fun read(subscriptionId: String): StoredSubscriptionDetails?

    suspend fun write(
        subscriptionId: String,
        details: StoredSubscriptionDetails,
    )
}

/**
 * Store backed by DataStore where it's supported, a no-op one on JS/Wasm
 */
internal expect fun createSubscriptionDetailsStore(platformContext: AiutaPlatformContext): SubscriptionDetailsStore

/**
 * The context a process-wide store may keep: on Android the application
 * context, so an Activity passed to Aiuta doesn't leak
 */
internal expect fun AiutaPlatformContext.toStorageContext(): AiutaPlatformContext
