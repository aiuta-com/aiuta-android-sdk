package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.delays

import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models.OperationDelay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

internal class OperationDelaysProviderTest {
    private val delays = listOf(OperationDelay(mode = "infinite", delay = 2.0))
    private val timeSource = TestTimeSource()

    private fun TestScope.provider(load: suspend () -> List<OperationDelay>?) = OperationDelaysProvider(
        scope = backgroundScope,
        timeSource = timeSource,
        loadTimeout = 10.seconds,
        load = load,
    )

    @Test
    fun concurrentRequestsShareOneLoad() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            delay(100)
            delays
        }

        val results = listOf(provider.prefetch(), provider.prefetch()).awaitAll()
        val cached = provider.getDelays(timeout = 1.seconds)

        assertEquals(1, loads)
        assertEquals(listOf(delays, delays), results)
        assertEquals(delays, cached)
    }

    @Test
    fun loadedDelaysAreCachedForHalfAnHourThenReloaded() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            delays
        }

        provider.getDelays(timeout = 1.seconds)
        timeSource += 29.minutes
        provider.getDelays(timeout = 1.seconds)
        assertEquals(1, loads)

        timeSource += 2.minutes
        provider.getDelays(timeout = 1.seconds)
        assertEquals(2, loads)
    }

    @Test
    fun responseWithoutDelaysIsCachedToo() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            null
        }

        assertNull(provider.getDelays(timeout = 1.seconds))
        assertNull(provider.getDelays(timeout = 1.seconds))
        assertEquals(1, loads)
    }

    @Test
    fun failedLoadReturnsNullAndIsRetriedNextTime() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            if (loads == 1) error("401") else delays
        }

        assertNull(provider.getDelays(timeout = 1.seconds))
        assertEquals(delays, provider.getDelays(timeout = 1.seconds))
        assertEquals(2, loads)
    }

    @Test
    fun slowLoadDoesNotBlockTheCallerButIsUsedOnceLoaded() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            delay(2.seconds)
            delays
        }

        // The caller gives up after its timeout and falls back to the default
        assertNull(provider.getDelays(timeout = 500.milliseconds))

        // The load keeps going in the background and is reused
        delay(2.seconds)
        assertEquals(delays, provider.getDelays(timeout = 500.milliseconds))
        assertEquals(1, loads)
    }

    @Test
    fun instanceIsSharedPerSubscription() {
        var created = 0
        val create = {
            created++
            OperationDelaysProvider(scope = TestScope(), load = { delays })
        }

        val first = OperationDelaysProvider.getInstance(subscriptionId = "shared-1", create = create)
        val same = OperationDelaysProvider.getInstance(subscriptionId = "shared-1", create = create)
        val other = OperationDelaysProvider.getInstance(subscriptionId = "shared-2", create = create)

        assertSame(first, same)
        assertNotSame(first, other)
        assertEquals(2, created)
    }

    @Test
    fun concurrentGetInstanceCreatesOneProvider() = runTest {
        var created = 0
        val create = {
            created++
            OperationDelaysProvider(scope = TestScope(), load = { delays })
        }

        val providers = withContext(Dispatchers.Default) {
            List(64) { async { OperationDelaysProvider.getInstance(subscriptionId = "concurrent", create = create) } }.awaitAll()
        }

        assertEquals(1, providers.toSet().size)
        assertEquals(1, created)
    }

    @Test
    fun loadThatTimesOutIsNotCached() = runTest {
        var loads = 0
        val provider = provider {
            loads++
            if (loads == 1) delay(1.minutes)
            delays
        }

        assertNull(provider.prefetch().await())
        assertEquals(delays, provider.getDelays(timeout = 1.seconds))
        assertEquals(2, loads)
    }
}
