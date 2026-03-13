package com.aiuta.fashionsdk.compose.uikit.utils

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaExperimentalSettings
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.logger.w

@Composable
internal actual fun rememberAiutaUriHandler(logger: AiutaLogger?): UriHandler {
    val context = LocalContext.current
    val defaultUriHandler = LocalUriHandler.current
    val shouldOpenInCustomTab = LocalAiutaExperimentalSettings.current.shouldOpenLinksInCustomTab

    val handler = remember(
        defaultUriHandler,
        logger,
        shouldOpenInCustomTab,
    ) {
        AndroidAiutaUriHandler(
            context = context,
            defaultUriHandler = defaultUriHandler,
            aiutaLogger = logger,
            shouldOpenInCustomTab = shouldOpenInCustomTab,
        )
    }

    DisposableEffect(handler) {
        onDispose { handler.unbind() }
    }

    return handler
}

internal class AndroidAiutaUriHandler(
    private val context: Context,
    private val defaultUriHandler: UriHandler,
    private val aiutaLogger: AiutaLogger?,
    private val shouldOpenInCustomTab: Boolean,
) : UriHandler {

    private var customTabsClient: CustomTabsClient? = null

    private val serviceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            client.warmup(0)
            customTabsClient = client
        }

        override fun onServiceDisconnected(name: ComponentName) {
            customTabsClient = null
        }
    }

    init {
        if (shouldOpenInCustomTab) {
            // Bind early so the client is ready before openUri is called.
            val packageName = CustomTabsClient.getPackageName(context, null)
            if (packageName != null) {
                CustomTabsClient.bindCustomTabsService(context, packageName, serviceConnection)
            }
        }
    }

    override fun openUri(uri: String) {
        try {
            aiutaLogger?.d("openUri(shouldOpenInCustomTab - $shouldOpenInCustomTab): try to open url - $uri")
            if (shouldOpenInCustomTab) {
                // Create a fresh session for each call.
                // A non-null session forces the URL into a Custom Tab, bypassing App Links.
                val session = customTabsClient?.newSession(null)
                val builder = CustomTabsIntent.Builder()
                if (session != null) {
                    builder.setSession(session)
                } else {
                    aiutaLogger?.w("openUri(): customTabsClient is null, opening without session")
                }
                builder.build().launchUrl(context, Uri.parse(uri))
            } else {
                defaultUriHandler.openUri(uri)
            }
            aiutaLogger?.d("openUri(): url $uri opened successfully")
        } catch (e: Exception) {
            aiutaLogger?.e("openUri(): failed to open url $uri", e)
        }
    }

    fun unbind() {
        try {
            context.unbindService(serviceConnection)
        } catch (_: Exception) {
            // ignore: service may have already disconnected
        }
        customTabsClient = null
    }
}
