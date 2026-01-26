@file:OptIn(ExperimentalForeignApi::class)

package com.aiuta.fashionsdk.internal.storage

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Native (iOS/macOS) implementation of storage path provider.
 *
 * Stores data in the Application Support directory:
 * `{Application Support}/datastore/{name}.preferences_pb`
 */
internal actual fun getStoragePath(name: String): String {
    val fileManager = NSFileManager.defaultManager
    val appSupportDirectory: NSURL? =
        fileManager.URLForDirectory(
            directory = NSApplicationSupportDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )

    val basePath =
        requireNotNull(appSupportDirectory?.path) {
            "Could not find Application Support directory"
        }

    val datastorePath = "$basePath/datastore"
    fileManager.createDirectoryAtPath(
        path = datastorePath,
        withIntermediateDirectories = true,
        attributes = null,
        error = null,
    )

    return "$datastorePath/$name.preferences_pb"
}
