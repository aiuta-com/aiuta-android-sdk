package com.aiuta.fashionsdk.internal.subscription.internal.store

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertSame

internal class StorageContextTest {
    @Test
    fun storageUsesTheApplicationContext() {
        val application = mockk<Context>()
        val activity = mockk<Context> { every { applicationContext } returns application }

        assertSame(application, activity.toStorageContext())
    }

    @Test
    fun contextWithoutApplicationContextIsUsedAsIs() {
        val context = mockk<Context> { every { applicationContext } returns null }

        assertSame(context, context.toStorageContext())
    }

    @Test
    fun storeResolvesTheApplicationContextRightAway() {
        val application = mockk<Context>()
        val activity = mockk<Context> { every { applicationContext } returns application }

        // Resolved at creation, not in the lazy storage initializer, so the
        // process-wide store never holds the Activity
        createSubscriptionDetailsStore(activity)

        verify(exactly = 1) { activity.applicationContext }
    }
}
