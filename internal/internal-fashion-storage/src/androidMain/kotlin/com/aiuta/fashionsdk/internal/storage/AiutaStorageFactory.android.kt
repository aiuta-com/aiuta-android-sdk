package com.aiuta.fashionsdk.internal.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.internal.getOrCreateStorage
import java.io.File

/**
 * Android implementation of platform storage factory.
 *
 * Creates DataStore files in the app's private directory:
 * `{context.filesDir}/datastore/{name}.preferences_pb`
 */
internal actual fun createPlatformStorage(
    name: String,
    platformContext: AiutaPlatformContext,
): AiutaStorage = getOrCreateStorage(name) {
    createDataStore(name, platformContext)
}

private fun createDataStore(
    name: String,
    context: Context,
): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    produceFile = {
        val directory = File(context.filesDir, "datastore").apply { mkdirs() }
        File(directory, "$name.preferences_pb")
    },
)
