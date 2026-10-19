package com.aiuta.fashionsdk.internal.subscription

import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

internal class InternalAiutaSubscriptionFactoryTest {
    private class FakeSubscription : InternalAiutaSubscription {
        override suspend fun preload() = Unit

        override suspend fun getDetails(timeout: Duration): AiutaSubscriptionDetails? = null
    }

    @Test
    fun instanceIsSharedPerSubscription() {
        var created = 0
        val create = {
            created++
            FakeSubscription()
        }

        val first = InternalAiutaSubscriptionFactory.getOrCreate(subscriptionId = "shared-1", create = create)
        val same = InternalAiutaSubscriptionFactory.getOrCreate(subscriptionId = "shared-1", create = create)
        val other = InternalAiutaSubscriptionFactory.getOrCreate(subscriptionId = "shared-2", create = create)

        assertSame(first, same)
        assertNotSame(first, other)
        assertEquals(2, created)
    }

    @Test
    fun concurrentCallsCreateOneInstance() = runTest {
        var created = 0
        val create = {
            created++
            // Slow creation widens the race window, so a missing lock is caught
            var work = 0L
            repeat(5_000_000) { work += it }
            check(work > 0)
            FakeSubscription()
        }

        val instances = withContext(Dispatchers.Default) {
            List(64) {
                async { InternalAiutaSubscriptionFactory.getOrCreate(subscriptionId = "concurrent", create = create) }
            }.awaitAll()
        }

        assertEquals(1, instances.toSet().size)
        assertEquals(1, created)
    }
}
