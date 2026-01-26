package com.aiuta.fashionsdk.internal.storage

import java.io.File

/**
 * JVM implementation of storage path provider.
 */
internal actual fun getStoragePath(name: String): String {
    val baseDir = File(System.getProperty("user.home"), ".aiuta/datastore").apply { mkdirs() }
    return File(baseDir, "$name.preferences_pb").absolutePath
}
