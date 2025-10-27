package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.picker

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils.fileProviderAuthority
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun newImageUri(
    context: Context,
    logger: AiutaLogger?,
    fileDateFormat: String = "yyyy_MM_dd_HH_mm_ss_SSS",
    fileExtension: String = "jpeg",
    locale: Locale = Locale.getDefault(),
): Uri? = try {
    logger?.d("newImageUri(): Creating new image uri")

    // Get path of images
    val directory = File(context.cacheDir, "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }

    // Get new file
    val file = File.createTempFile(
        SimpleDateFormat(
            fileDateFormat,
            locale,
        ).format(Date()),
        ".$fileExtension",
        directory,
    )
    // Authority of provider

    // Return uri of the file
    FileProvider.getUriForFile(
        context,
        context.fileProviderAuthority(),
        file,
    )
} catch (e: Exception) {
    // Fallback with null
    logger?.e("Failed to create new image uri - $e", e)
    null
}
