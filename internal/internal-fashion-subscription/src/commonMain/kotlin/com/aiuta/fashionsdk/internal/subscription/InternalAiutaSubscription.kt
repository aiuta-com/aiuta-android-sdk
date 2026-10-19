package com.aiuta.fashionsdk.internal.subscription

import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import kotlin.time.Duration

/**
 * Subscription configuration from the backend, shared by the SDK modules.
 *
 * The details are loaded once and cached: in memory for 30 minutes and on
 * disk between launches (Android, iOS and JVM), revalidated with an ETag.
 * Cached details are served right away, even stale ones, while a refresh runs
 * in the background.
 */
public interface InternalAiutaSubscription {
    /**
     * Starts refreshing the details in the background, unless they are fresh
     * or already loading. Call it early, e.g. when a flow opens, so they are
     * ready once needed. Never throws.
     */
    public suspend fun preload()

    /**
     * The subscription details: cached ones right away (refreshing them in the
     * background when stale), otherwise the backend response if it comes
     * within [timeout].
     *
     * @return null when nothing is cached and the backend failed or didn't
     * answer within [timeout]
     */
    public suspend fun getDetails(timeout: Duration): AiutaSubscriptionDetails?
}
