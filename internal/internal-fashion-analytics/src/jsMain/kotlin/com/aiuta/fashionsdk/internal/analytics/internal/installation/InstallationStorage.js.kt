package com.aiuta.fashionsdk.internal.analytics.internal.installation

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import kotlinx.browser.localStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal actual fun buildInstallationStorage(
    platformContext: AiutaPlatformContext,
): InstallationStorage = JsInstallationStorage()

internal class JsInstallationStorage : InstallationStorage {
    override suspend fun readInstallationId(): String? = withContext(Dispatchers.Default) {
        localStorage.getItem(INSTALLATION_FILE_NAME)
    }

    override suspend fun writeInstallationId(id: String) {
        withContext(Dispatchers.Default) {
            localStorage.setItem(INSTALLATION_FILE_NAME, id)
        }
    }
}
