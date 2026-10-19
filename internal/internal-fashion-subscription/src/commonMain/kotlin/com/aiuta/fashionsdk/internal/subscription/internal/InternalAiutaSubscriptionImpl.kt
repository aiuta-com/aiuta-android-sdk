package com.aiuta.fashionsdk.internal.subscription.internal

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.subscription.InternalAiutaSubscription
import com.aiuta.fashionsdk.internal.subscription.internal.remote.KtorSubscriptionDetailsRemoteDataSource
import com.aiuta.fashionsdk.internal.subscription.internal.remote.SubscriptionDetailsRemoteDataSource
import com.aiuta.fashionsdk.internal.subscription.internal.remote.SubscriptionDetailsResponse
import com.aiuta.fashionsdk.internal.subscription.internal.store.StoredSubscriptionDetails
import com.aiuta.fashionsdk.internal.subscription.internal.store.SubscriptionDetailsStore
import com.aiuta.fashionsdk.internal.subscription.internal.store.createSubscriptionDetailsStore
import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.network.defaultNetworkClient
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

internal class InternalAiutaSubscriptionImpl(
    private val subscriptionId: String,
    private val remoteDataSource: SubscriptionDetailsRemoteDataSource,
    private val store: SubscriptionDetailsStore,
    private val scope: CoroutineScope,
    private val logger: AiutaLogger? = null,
    private val timeSource: TimeSource = TimeSource.Monotonic,
    private val loadTimeout: Duration = LOAD_TIMEOUT,
) : InternalAiutaSubscription,
    AutoCloseable {
    private val mutex = Mutex()

    // Last known details (from disk or the backend) and when the backend last
    // confirmed them in this process
    private var isRestored = false
    private var cached: StoredSubscriptionDetails? = null
    private var refreshedAt: TimeMark? = null
    private var failedAt: TimeMark? = null
    private var refresh: Deferred<AiutaSubscriptionDetails?>? = null

    override suspend fun preload() {
        startRefreshIfNeeded()
    }

    override suspend fun getDetails(timeout: Duration): AiutaSubscriptionDetails? {
        val refresh = startRefreshIfNeeded()

        // Known details are served right away, even stale ones, while the
        // refresh runs in the background
        mutex.withLock { cached?.details }?.let { return it }

        return withTimeoutOrNull(timeout) {
            try {
                refresh.await()
            } catch (e: CancellationException) {
                // A closed instance cancels its refresh; the caller itself goes on
                currentCoroutineContext().ensureActive()
                null
            }
        }
    }

    private suspend fun startRefreshIfNeeded(): Deferred<AiutaSubscriptionDetails?> {
        restoreOnce()

        return mutex.withLock {
            val current = refresh
            val isLoading = current != null && !current.isCompleted
            val isFresh = refreshedAt?.let { it.elapsedNow() < CACHE_DURATION } == true
            // After a failure the backend isn't asked again for a while, so an
            // outage doesn't turn every screen and generation into a request
            val isBackingOff = failedAt?.let { it.elapsedNow() < FAILURE_BACKOFF } == true
            if (current != null && (isLoading || isFresh || isBackingOff)) return@withLock current

            refreshedAt = null
            scope.async { load() }.also { refresh = it }
        }
    }

    private suspend fun restoreOnce() {
        if (mutex.withLock { isRestored }) return

        val stored = try {
            store.read(subscriptionId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger?.e("InternalAiutaSubscription: failed to read stored details", e)
            null
        }

        mutex.withLock {
            if (isRestored) return@withLock
            isRestored = true
            // A backend response may already be here: it wins over the disk
            if (cached == null) cached = stored
        }
    }

    private suspend fun load(): AiutaSubscriptionDetails? {
        val etag = mutex.withLock { cached?.etag }

        // null: the request failed or timed out, so the details aren't marked
        // fresh and the next call retries
        val response = withTimeoutOrNull(loadTimeout) {
            try {
                remoteDataSource.getSubscriptionDetails(etag)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger?.e("InternalAiutaSubscription: failed to load details", e)
                null
            }
        }

        val (details, changed) = mutex.withLock {
            when (response) {
                is SubscriptionDetailsResponse.Modified -> {
                    val stored = StoredSubscriptionDetails(etag = response.etag, details = response.details)
                    cached = stored
                    refreshedAt = timeSource.markNow()
                    failedAt = null
                    stored.details to stored
                }

                SubscriptionDetailsResponse.NotModified -> {
                    refreshedAt = timeSource.markNow()
                    failedAt = null
                    cached?.details to null
                }

                null -> {
                    failedAt = timeSource.markNow()
                    cached?.details to null
                }
            }
        }

        changed?.let { persist(it) }
        return details
    }

    private suspend fun persist(details: StoredSubscriptionDetails) {
        try {
            store.write(subscriptionId, details)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger?.e("InternalAiutaSubscription: failed to store details", e)
        }
    }

    /**
     * Stops the in-flight refresh; called when this instance is replaced by
     * one for another subscription
     */
    override fun close() {
        scope.cancel()
    }

    companion object {
        private val CACHE_DURATION = 30.minutes
        private val FAILURE_BACKOFF = 30.seconds
        private val LOAD_TIMEOUT = 10.seconds

        /**
         * The instance outlives this Aiuta, so it keeps only what it needs:
         * holding Aiuta would leak its platform (Android) context
         */
        fun create(aiuta: Aiuta): InternalAiutaSubscriptionImpl = InternalAiutaSubscriptionImpl(
            subscriptionId = aiuta.authenticationStrategy.subscriptionId,
            remoteDataSource = KtorSubscriptionDetailsRemoteDataSource(
                networkClient = defaultNetworkClient(aiuta = aiuta),
            ),
            store = createSubscriptionDetailsStore(aiuta.platformContext),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
            logger = aiuta.logger,
        )
    }
}
