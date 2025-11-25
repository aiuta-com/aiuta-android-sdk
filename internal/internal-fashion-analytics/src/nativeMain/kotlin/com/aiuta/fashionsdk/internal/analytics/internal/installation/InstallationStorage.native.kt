package com.aiuta.fashionsdk.internal.analytics.internal.installation

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

internal actual fun buildInstallationStorage(
    platformContext: AiutaPlatformContext,
): InstallationStorage = IosInstallationStorage()

internal class IosInstallationStorage : InstallationStorage {
    private fun filePath(): String {
        val dir =
            NSSearchPathForDirectoriesInDomains(
                directory = NSDocumentDirectory,
                domainMask = NSUserDomainMask,
                expandTilde = true,
            ).first() as String
        return "$dir/${INSTALLATION_FILE_NAME}"
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun readInstallationId(): String? {
        return withContext(Dispatchers.IO) {
            val path = filePath()
            val fileManager = NSFileManager.defaultManager
            if (!fileManager.fileExistsAtPath(path)) return@withContext null

            return@withContext NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null)
                ?.toString()
        }
    }

    override suspend fun writeInstallationId(id: String) {
        withContext(Dispatchers.IO) {
            val path = filePath()
            NSString.create(string = id).writeToFile(path, true)
        }
    }
}
