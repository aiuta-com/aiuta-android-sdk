package com.aiuta.fashionsdk.internal.subscription.internal.store

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import com.aiuta.fashionsdk.internal.subscription.model.AiutaPoweredBySticker
import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import java.io.File
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Real DataStore round-trips (on JVM it lives in ~/.aiuta/datastore) */
internal class StorageSubscriptionDetailsStoreTest {
    private val namespace = "subscription_store_test_${Random.nextLong().toULong()}"
    private val storage = AiutaStorage.Factory().create(namespace, AiutaPlatformContext.INSTANCE)
    private val store = StorageSubscriptionDetailsStore(storage = lazyOf(storage))

    @AfterTest
    fun cleanUp() {
        File(System.getProperty("user.home"), ".aiuta/datastore/$namespace.preferences_pb").delete()
    }

    @Test
    fun detailsSurviveAWriteReadRoundTrip() = runTest {
        store.write(
            subscriptionId = "subscription",
            details = StoredSubscriptionDetails(
                etag = "\"v1\"",
                details = AiutaSubscriptionDetails(
                    poweredBySticker = AiutaPoweredBySticker(urlAndroid = "https://aiuta.com/android", isVisible = true),
                    operationDelaysSequence = listOf(
                        AiutaOperationDelay(mode = "recurring", delay = 0.3, repeat = 30),
                        AiutaOperationDelay(mode = "infinite", delay = 3.0),
                    ),
                ),
            ),
        )

        val stored = assertNotNull(store.read("subscription"))
        assertEquals("\"v1\"", stored.etag)
        assertEquals("https://aiuta.com/android", stored.details.poweredBySticker?.urlAndroid)
        assertEquals(true, stored.details.poweredBySticker?.isVisible)
        assertEquals(
            listOf(Triple("recurring", 0.3, 30), Triple("infinite", 3.0, null)),
            stored.details.operationDelaysSequence?.map { Triple(it.mode, it.delay, it.repeat) },
        )
    }

    @Test
    fun subscriptionsAreStoredSeparately() = runTest {
        store.write("first", StoredSubscriptionDetails(etag = "\"first\"", details = AiutaSubscriptionDetails()))

        assertEquals("\"first\"", store.read("first")?.etag)
        assertNull(store.read("second"))
    }

    @Test
    fun detailsStoredByANewerVersionWithMoreFieldsStillRead() = runTest {
        // A newer SDK may store fields this one doesn't know yet
        storage.save(
            key = "details_subscription",
            value = NewerStoredDetails(
                etag = "\"v2\"",
                details = NewerDetails(retryCounts = mapOf("photo_upload" to 2)),
            ),
            serializer = NewerStoredDetails.serializer(),
        )

        assertEquals("\"v2\"", store.read("subscription")?.etag)
    }

    @Serializable
    private data class NewerDetails(
        @SerialName("retry_counts")
        val retryCounts: Map<String, Int>,
    )

    @Serializable
    private data class NewerStoredDetails(
        @SerialName("etag")
        val etag: String?,
        @SerialName("details")
        val details: NewerDetails,
        @SerialName("stored_at")
        val storedAt: Long = 0,
    )
}
