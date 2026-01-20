@file:OptIn(ExperimentalForeignApi::class)

package com.aiuta.fashionsdk.internal.storage

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Native (iOS/macOS) implementation of storage path provider.
 *
 * Stores data in the Documents directory:
 * `{Documents}/datastore/{name}.preferences_pb`
 */
internal actual fun getStoragePath(name: String): String {
    val fileManager = NSFileManager.defaultManager
    val documentDirectory: NSURL? = fileManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    val basePath = requireNotNull(documentDirectory?.path) {
        "Could not find Documents directory"
    }

    return "$basePath/datastore/$name.preferences_pb"
}
