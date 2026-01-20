package com.aiuta.fashionsdk.internal.storage

import java.io.File

/**
 * JVM implementation of storage path provider.
 */
internal actual fun getStoragePath(name: String): String {
    val file = File(System.getProperty("java.io.tmpdir"), "$name.preferences_pb")
    return file.absolutePath
}
