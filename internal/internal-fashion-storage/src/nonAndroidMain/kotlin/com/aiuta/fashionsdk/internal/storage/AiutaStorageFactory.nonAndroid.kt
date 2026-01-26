package com.aiuta.fashionsdk.internal.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.internal.getOrCreateStorage
import okio.Path.Companion.toPath

/**
 * Non-Android implementation of platform storage factory.
 *
 * Uses Okio paths for DataStore file creation on JVM, iOS, JS, and WASM platforms.
 * Files are stored in a platform-specific location determined by [getStoragePath].
 */
internal actual fun createPlatformStorage(
    name: String,
    platformContext: AiutaPlatformContext,
): AiutaStorage = getOrCreateStorage(name) {
    createDataStore(name)
}

private fun createDataStore(name: String): DataStore<Preferences> {
    val path = getStoragePath(name)
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() },
    )
}

/**
 * Returns platform-specific storage path for the given namespace.
 *
 * This function is implemented differently on each non-Android platform
 * to provide appropriate storage locations.
 */
internal expect fun getStoragePath(name: String): String
