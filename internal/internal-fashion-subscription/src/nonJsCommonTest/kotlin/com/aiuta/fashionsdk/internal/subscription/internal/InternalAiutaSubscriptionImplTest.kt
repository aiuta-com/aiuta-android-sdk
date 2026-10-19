package com.aiuta.fashionsdk.internal.subscription.internal

import com.aiuta.fashionsdk.internal.subscription.internal.remote.SubscriptionDetailsRemoteDataSource
import com.aiuta.fashionsdk.internal.subscription.internal.remote.SubscriptionDetailsResponse
import com.aiuta.fashionsdk.internal.subscription.internal.store.StoredSubscriptionDetails
import com.aiuta.fashionsdk.internal.subscription.internal.store.SubscriptionDetailsStore
import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

internal class InternalAiutaSubscriptionImplTest {
    private val fresh = details(delay = 0.3)
    private val stale = details(delay = 1.0)
    private val timeSource = TestTimeSource()

    private fun details(delay: Double) = AiutaSubscriptionDetails(
        operationDelaysSequence = listOf(AiutaOperationDelay(mode = "infinite", delay = delay)),
    )

    /** Remote answering with [responses] in order; null fails the request */
    private class FakeRemote(
        private val responses: List<SubscriptionDetailsResponse?>,
        private val latency: Long = 0,
    ) : SubscriptionDetailsRemoteDataSource {
        val etags = mutableListOf<String?>()

        override suspend fun getSubscriptionDetails(etag: String?): SubscriptionDetailsResponse {
            etags += etag
            delay(latency)
            return responses[etags.size - 1] ?: error("Network error")
        }
    }

    private class FakeStore(var stored: StoredSubscriptionDetails? = null) : SubscriptionDetailsStore {
        var writes = 0

        override suspend fun read(subscriptionId: String): StoredSubscriptionDetails? = stored

        override suspend fun write(
            subscriptionId: String,
            details: StoredSubscriptionDetails,
        ) {
            writes++
            stored = details
        }
    }

    private fun TestScope.subscription(
        remote: FakeRemote,
        store: FakeStore = FakeStore(),
    ) = InternalAiutaSubscriptionImpl(
        subscriptionId = "subscription",
        remoteDataSource = remote,
        store = store,
        scope = backgroundScope,
        timeSource = timeSource,
        loadTimeout = 10.seconds,
    )

    private fun modified(
        details: AiutaSubscriptionDetails,
        etag: String = "\"v2\"",
    ) = SubscriptionDetailsResponse.Modified(details = details, etag = etag)

    @Test
    fun concurrentCallsShareOneLoad() = runTest {
        val remote = FakeRemote(listOf(modified(fresh)), latency = 100)
        val subscription = subscription(remote)

        val results = List(3) { async { subscription.getDetails(timeout = 1.seconds) } }.awaitAll()

        assertEquals(List(3) { fresh }, results)
        assertEquals(1, remote.etags.size)
    }

    @Test
    fun storedDetailsAreServedRightAwayAndRevalidatedByTheirEtag() = runTest {
        val remote = FakeRemote(listOf(modified(fresh)), latency = 5_000)
        val store = FakeStore(StoredSubscriptionDetails(etag = "\"v1\"", details = stale))
        val subscription = subscription(remote, store)

        // No waiting for the slow backend: the stored details come back at once
        assertEquals(stale, subscription.getDetails(timeout = 1.seconds))
        assertEquals(0L, testScheduler.currentTime)

        delay(1.minutes) // virtual time; also runs the background refresh
        assertEquals(listOf<String?>("\"v1\""), remote.etags)
        assertEquals(fresh, subscription.getDetails(timeout = 1.seconds))
        assertEquals(StoredSubscriptionDetails(etag = "\"v2\"", details = fresh), store.stored)
    }

    @Test
    fun notModifiedKeepsDetailsFreshWithoutRewritingTheStore() = runTest {
        val remote = FakeRemote(listOf(SubscriptionDetailsResponse.NotModified))
        val store = FakeStore(StoredSubscriptionDetails(etag = "\"v1\"", details = stale))
        val subscription = subscription(remote, store)

        subscription.preload()
        delay(1.minutes) // virtual time; also runs the background refresh

        assertEquals(stale, subscription.getDetails(timeout = 1.seconds))
        assertEquals(1, remote.etags.size)
        assertEquals(0, store.writes)
    }

    @Test
    fun detailsAreFreshForHalfAnHourThenRevalidated() = runTest {
        val remote = FakeRemote(listOf(modified(stale, etag = "\"v1\""), modified(fresh)))
        val subscription = subscription(remote)

        subscription.getDetails(timeout = 1.seconds)
        timeSource += 29.minutes
        subscription.getDetails(timeout = 1.seconds)
        assertEquals(1, remote.etags.size)

        timeSource += 2.minutes
        // Stale details still come back right away while revalidating
        assertEquals(stale, subscription.getDetails(timeout = 1.seconds))
        delay(1.minutes) // virtual time; also runs the background refresh
        assertEquals(listOf(null, "\"v1\""), remote.etags)
        assertEquals(fresh, subscription.getDetails(timeout = 1.seconds))
    }

    @Test
    fun failedLoadIsRetriedAndKeepsTheKnownDetails() = runTest {
        val remote = FakeRemote(listOf(modified(stale, etag = "\"v1\""), null, modified(fresh)))
        val store = FakeStore()
        val subscription = subscription(remote, store)

        subscription.getDetails(timeout = 1.seconds)
        timeSource += 31.minutes

        // The refresh fails: the known details stay and aren't marked fresh
        assertEquals(stale, subscription.getDetails(timeout = 1.seconds))
        delay(1.minutes) // virtual time; also runs the background refresh
        assertEquals(1, store.writes)

        // So the next call retries it
        subscription.getDetails(timeout = 1.seconds)
        delay(1.minutes) // virtual time; also runs the background refresh
        assertEquals(3, remote.etags.size)
        assertEquals(fresh, subscription.getDetails(timeout = 1.seconds))
    }

    @Test
    fun withoutCachedDetailsCallerWaitsOnlyUpToItsTimeout() = runTest {
        val remote = FakeRemote(listOf(modified(fresh)), latency = 2_000)
        val subscription = subscription(remote)

        assertNull(subscription.getDetails(timeout = 500.milliseconds))

        // The load keeps going in the background and is reused
        delay(1.minutes) // virtual time; also runs the background refresh
        assertEquals(fresh, subscription.getDetails(timeout = 500.milliseconds))
        assertEquals(1, remote.etags.size)
    }

    @Test
    fun loadThatTimesOutIsRetried() = runTest {
        var calls = 0
        val remote = object : SubscriptionDetailsRemoteDataSource {
            override suspend fun getSubscriptionDetails(etag: String?): SubscriptionDetailsResponse {
                calls++
                if (calls == 1) delay(1.minutes)
                return modified(fresh)
            }
        }
        val subscription = InternalAiutaSubscriptionImpl(
            subscriptionId = "subscription",
            remoteDataSource = remote,
            store = FakeStore(),
            scope = backgroundScope,
            timeSource = timeSource,
            loadTimeout = 10.seconds,
        )

        // The first load hangs and gives up after the 10 s load timeout
        assertNull(subscription.getDetails(timeout = 1.minutes))
        assertEquals(10_000L, testScheduler.currentTime)

        // So the next call loads again
        assertEquals(fresh, subscription.getDetails(timeout = 1.seconds))
        assertEquals(2, calls)
    }
}
