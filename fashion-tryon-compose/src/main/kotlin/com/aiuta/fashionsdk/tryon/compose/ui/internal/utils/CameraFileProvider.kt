package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun newImageUri(
    context: Context,
    fileDateFormat: String = "yyyy_MM_dd_hh_mm_ss_SSSZ",
    fileExtension: String = "jpeg",
    locale: Locale = Locale.getDefault(),
): Uri? {
    return try {
        // Get path of images
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()

        // Get new file
        val file =
            File.createTempFile(
                SimpleDateFormat(
                    fileDateFormat,
                    locale,
                ).format(Date()),
                ".$fileExtension",
                directory,
            )
        // Authority of provider
        val authority = "${context.packageName}.aiuta.tryon.compose.fileprovider"

        // Return uri of the file
        FileProvider.getUriForFile(
            context,
            authority,
            file,
        )
    } catch (e: Exception) {
        // Fallback with null
        null
    }
}
