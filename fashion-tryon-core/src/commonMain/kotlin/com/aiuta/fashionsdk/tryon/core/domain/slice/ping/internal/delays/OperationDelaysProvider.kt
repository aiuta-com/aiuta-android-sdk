package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.delays

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models.OperationDelay
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.subscriptionDetailsDataSourceFactory
import kotlin.concurrent.Volatile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Delays between operation status polls, configured by the backend in
 * `subscription_details` → `operation_delays_sequence`.
 *
 * Loaded in the background and kept in memory for [CACHE_DURATION]; a failed
 * or timed out load isn't cached, so the next generation retries it. Callers
 * fall back to the default delays whenever the backend ones aren't available.
 */
internal class OperationDelaysProvider(
    private val scope: CoroutineScope,
    private val timeSource: TimeSource = TimeSource.Monotonic,
    private val loadTimeout: Duration = LOAD_TIMEOUT,
    private val load: suspend () -> List<OperationDelay>?,
) {
    private val mutex = Mutex()
    private var request: Deferred<List<OperationDelay>?>? = null
    private var loadedAt: TimeMark? = null

    /**
     * Starts loading the delays unless they are loading or were loaded
     * within [CACHE_DURATION]
     */
    suspend fun prefetch(): Deferred<List<OperationDelay>?> = mutex.withLock {
        val current = request
        val isLoading = current != null && !current.isCompleted
        val isFresh = loadedAt?.let { it.elapsedNow() < CACHE_DURATION } == true
        if (current != null && (isLoading || isFresh)) return current

        loadedAt = null
        scope.async {
            // null outcome: the load timed out
            val outcome = withTimeoutOrNull(loadTimeout) {
                try {
                    Result.success(load())
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            // Only a successful load is cached, even one without delays
            if (outcome?.isSuccess == true) mutex.withLock { loadedAt = timeSource.markNow() }
            outcome?.getOrNull()
        }.also { request = it }
    }

    /**
     * The backend delays, or null when they are missing, failed to load or
     * didn't load within [timeout]
     */
    suspend fun getDelays(timeout: Duration): List<OperationDelay>? = withTimeoutOrNull(timeout) {
        prefetch().await()
    }

    companion object : SynchronizedObject() {
        private val CACHE_DURATION = 30.minutes
        private val LOAD_TIMEOUT = 10.seconds

        @Volatile
        private var cached: Pair<String, OperationDelaysProvider>? = null

        /**
         * Process-wide instance per subscription, like the network client,
         * so every generation shares the loaded delays
         */
        fun getInstance(aiuta: Aiuta): OperationDelaysProvider = getInstance(
            subscriptionId = aiuta.authenticationStrategy.subscriptionId,
        ) {
            // The provider outlives this Aiuta, so it keeps only the data
            // source: holding Aiuta would leak its platform (Android) context
            val dataSource = aiuta.subscriptionDetailsDataSourceFactory
            OperationDelaysProvider(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                load = { dataSource.getOperationDelaysDetails().operationDelaysSequence },
            )
        }

        internal fun getInstance(
            subscriptionId: String,
            create: () -> OperationDelaysProvider,
        ): OperationDelaysProvider {
            cached?.let { (cachedSubscriptionId, provider) ->
                if (cachedSubscriptionId == subscriptionId) return provider
            }

            return synchronized(this) {
                cached?.takeIf { (cachedSubscriptionId) -> cachedSubscriptionId == subscriptionId }?.second
                    ?: create().also { cached = subscriptionId to it }
            }
        }
    }
}
