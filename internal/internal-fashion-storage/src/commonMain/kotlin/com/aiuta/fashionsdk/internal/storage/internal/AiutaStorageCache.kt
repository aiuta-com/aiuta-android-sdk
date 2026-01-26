package com.aiuta.fashionsdk.internal.storage.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

internal object StorageCacheLock : SynchronizedObject() {
    val instances = mutableMapOf<String, AiutaStorage>()
}

internal inline fun getOrCreateStorage(
    name: String,
    crossinline dataStoreFactory: () -> DataStore<Preferences>,
): AiutaStorage = synchronized(StorageCacheLock) {
    StorageCacheLock.instances.getOrPut(name) {
        DataStoreStorage(dataStoreFactory())
    }
}
