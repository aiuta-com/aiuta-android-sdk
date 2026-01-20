package com.aiuta.fashionsdk.internal.storage

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlinx.coroutines.test.runTest

class AiutaStorageFactoryCacheTest {

    private val platformContext: AiutaPlatformContext = AiutaPlatformContext.INSTANCE

    @Test
    fun sameNamespaceReturnsCachedInstance() = runTest {
        val namespace = uniqueNamespace("cache_same")
        val storage1 = AiutaStorageFactory.create(namespace, platformContext)
        val storage2 = AiutaStorageFactory.create(namespace, platformContext)

        assertSame(storage1, storage2)

        storage1.saveString("key", "value")
        assertEquals("value", storage2.getString("key"))
    }

    @Test
    fun differentNamespacesProduceIndependentInstances() = runTest {
        val namespace1 = uniqueNamespace("cache_first")
        val namespace2 = uniqueNamespace("cache_second")
        val storage1 = AiutaStorageFactory.create(namespace1, platformContext)
        val storage2 = AiutaStorageFactory.create(namespace2, platformContext)

        assertNotSame(storage1, storage2)

        storage1.saveString("key", "first")
        storage2.saveString("key", "second")

        assertEquals("first", storage1.getString("key"))
        assertEquals("second", storage2.getString("key"))
    }

    private fun uniqueNamespace(prefix: String): String = "${prefix}_${Random.nextInt()}"
}
