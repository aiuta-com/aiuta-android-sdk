package com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsShareEvent
import com.aiuta.fashionsdk.analytics.events.AiutaShareEventType
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalyticFactory
import kotlinx.serialization.json.Json

/**
 * This function takes a title, content, and contentUri as input parameters
 * and shares the image with the specified title and content on the available
 * sharing platforms.
 */

internal fun Context.shareContent(
    content: String?,
    pageId: AiutaAnalyticsPageId,
    productIds: List<String>,
    fileUris: List<Uri> = emptyList(),
) {
    // Create a new Intent fileUris with the ACTION_SEND action.
    val action = if (fileUris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
    val intent = Intent(action)
    // Set the MIME type of the Intent to "image/png", "*/*" or text/plain
    intent.type =
        when (fileUris.size) {
            0 -> "text/plain"
            else -> "image/*"
        }

    // Init back receiver
    val pi =
        PendingIntent.getBroadcast(
            this,
            SHARE_REQUEST_CODE,
            Intent(this, ShareBroadcastReceiver::class.java).apply {
                putExtra(PAGE_ID_KEY, Json.encodeToString(pageId))
                putStringArrayListExtra(PRODUCT_IDS_KEY, ArrayList(productIds))
            },
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    // Add the content as extra data to the Intent.
    content?.let {
        intent.putExtra(Intent.EXTRA_TEXT, content)
    }
    // Add the content URIs as extra data to the Intent.
    if (fileUris.size == 1) {
        intent.putExtra(Intent.EXTRA_STREAM, fileUris.first())
    } else if (fileUris.size > 1) {
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(fileUris))
    }
    // Add a flag to grant read permission to the receiving app for the content URI.
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // Create a chooser Intent and start the activity.
    val chooserIntent =
        Intent
            .createChooser(intent, null, pi.intentSender)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(chooserIntent)
}

internal class ShareBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val clickedComponent: ComponentName? =
            intent?.getParcelableExtra(
                Intent.EXTRA_CHOSEN_COMPONENT,
            )
        val pageId = intent?.getStringExtra(PAGE_ID_KEY)
        val productIds = intent?.getStringArrayListExtra(PRODUCT_IDS_KEY)

        InternalAiutaAnalyticFactory.getInternalAiutaAnalytic()?.sendEvent(
            event = AiutaAnalyticsShareEvent(
                pageId = pageId?.let { Json.decodeFromString(it) },
                productIds = productIds?.toList().orEmpty(),
                targetId = clickedComponent?.packageName,
                event = AiutaShareEventType.SUCCEEDED,
            ),
        )
    }
}
