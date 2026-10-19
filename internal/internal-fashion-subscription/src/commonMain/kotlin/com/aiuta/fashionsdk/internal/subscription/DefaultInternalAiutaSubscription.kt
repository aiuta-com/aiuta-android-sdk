package com.aiuta.fashionsdk.internal.subscription

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.subscription.internal.InternalAiutaSubscriptionImpl
import kotlin.concurrent.Volatile
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/**
 * Extension for default creation of [InternalAiutaSubscription].
 *
 * Will cache instance of [InternalAiutaSubscription] per subscription
 */
public val Aiuta.internalAiutaSubscription: InternalAiutaSubscription
    get() = InternalAiutaSubscriptionFactory.create(this)

/**
 * Process-wide factory of [InternalAiutaSubscription], so every SDK module
 * shares one cache of the subscription details
 */
public object InternalAiutaSubscriptionFactory : SynchronizedObject() {
    @Volatile
    private var cached: Pair<String, InternalAiutaSubscription>? = null

    /**
     * Returns the instance for the subscription of [aiuta], creating it on
     * the first call or when the subscription changes
     */
    public fun create(aiuta: Aiuta): InternalAiutaSubscription = getOrCreate(
        subscriptionId = aiuta.authenticationStrategy.subscriptionId,
    ) {
        InternalAiutaSubscriptionImpl.create(aiuta)
    }

    internal fun getOrCreate(
        subscriptionId: String,
        create: () -> InternalAiutaSubscription,
    ): InternalAiutaSubscription {
        cached?.let { (cachedSubscriptionId, subscription) ->
            if (cachedSubscriptionId == subscriptionId) return subscription
        }

        return synchronized(this) {
            cached?.takeIf { (cachedSubscriptionId) -> cachedSubscriptionId == subscriptionId }?.second
                ?: create().also { cached = subscriptionId to it }
        }
    }
}
