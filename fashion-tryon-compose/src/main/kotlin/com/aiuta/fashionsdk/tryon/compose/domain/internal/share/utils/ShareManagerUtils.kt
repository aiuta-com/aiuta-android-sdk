package com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aiuta.fashionsdk.internal.analytic.InternalAiutaAnalyticFactory
import com.aiuta.fashionsdk.internal.analytic.model.ShareGeneratedImage
import com.aiuta.fashionsdk.internal.analytic.model.ShareSuccessfully

private const val ORIGIN_KEY = "originKey"
private const val COUNT_KEY = "countKey"
private const val SHARE_REQUEST_CODE = 100

/**
 * This function takes a title, content, and contentUri as input parameters
 * and shares the image with the specified title and content on the available
 * sharing platforms.
 */

internal fun Context.shareContent(
    content: String?,
    contentUris: ArrayList<Uri> = arrayListOf(),
    origin: ShareGeneratedImage.Origin,
) {
    // Create a new Intent object with the ACTION_SEND action.
    val action = if (contentUris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
    val intent = Intent(action)
    // Set the MIME type of the Intent to "image/png", "*/*" or text/plain
    intent.type =
        when (contentUris.size) {
            0 -> "text/plain"
            else -> "image/*"
        }

    // Init back receiver
    val pi =
        PendingIntent.getBroadcast(
            this,
            SHARE_REQUEST_CODE,
            Intent(this, ShareBroadcastReceiver::class.java).apply {
                putExtra(COUNT_KEY, contentUris.size)
                putExtra(ORIGIN_KEY, origin.value)
                putExtra(Intent.EXTRA_TEXT, content)
            },
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    // Add the content as extra data to the Intent.
    content?.let {
        intent.putExtra(Intent.EXTRA_TEXT, content)
    }
    // Add the content URIs as extra data to the Intent.
    if (contentUris.size == 1) {
        intent.putExtra(Intent.EXTRA_STREAM, contentUris.first())
    } else if (contentUris.size > 1) {
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, contentUris)
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
        val count = intent?.getIntExtra(COUNT_KEY, 0)
        val origin = intent?.getStringExtra(ORIGIN_KEY)
        val additionalShareInfo = intent?.getStringExtra(Intent.EXTRA_TEXT)

        InternalAiutaAnalyticFactory.getInternalAiutaAnalytic()?.sendEvent(
            event =
                ShareSuccessfully(
                    origin = origin,
                    count = count.toString(),
                    target = clickedComponent?.packageName,
                    additionalShareInfo = additionalShareInfo,
                ),
        )
    }
}
