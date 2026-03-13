package com.aiuta.fashionsdk.compose.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e

@Composable
internal fun rememberAiutaUriHandler(logger: AiutaLogger?): UriHandler {
    val defaultUriHandler = LocalUriHandler.current

    return remember(
        defaultUriHandler,
        logger,
    ) {
        LoggingAiutaUriHandler(
            defaultUriHandler = defaultUriHandler,
            aiutaLogger = logger,
        )
    }
}

internal class LoggingAiutaUriHandler(
    private val defaultUriHandler: UriHandler,
    private val aiutaLogger: AiutaLogger?,
) : UriHandler {

    // Use default handler with logs
    override fun openUri(uri: String) {
        try {
            aiutaLogger?.d("openUri(): try to open url - $uri")
            defaultUriHandler.openUri(uri)
            aiutaLogger?.d("openUri(): url $uri opened successfully")
        } catch (e: Exception) {
            aiutaLogger?.e("openUri(): failed to open url $uri", e)
        }
    }
}
