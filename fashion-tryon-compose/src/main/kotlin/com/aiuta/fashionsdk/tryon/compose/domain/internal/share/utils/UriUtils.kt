package com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal fun Context.getUriFromBitmap(
    bmp: Bitmap,
    filePrefix: String = "image",
    qualityPercent: Int = 100,
    isCache: Boolean,
): Uri? {
    var bmpUri: Uri? = null // Initialize the Uri object to null.
    try {
        val fileName = "${filePrefix}_${System.currentTimeMillis()}.png"
        // Create a new file with a unique name in the Pictures directory of the app's external files directory.
        val file =
            File(
                if (isCache) cacheDir else getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                fileName,
            )
        // Create a FileOutputStream object to write the bitmap data to the file.
        val out = FileOutputStream(file)
        // Compress the bitmap data and write it to the FileOutputStream object.
        bmp.compress(Bitmap.CompressFormat.PNG, qualityPercent, out)
        // Close the FileOutputStream object.
        out.close()
        // Use the FileProvider class to get a content URI for the file.
        bmpUri =
            FileProvider.getUriForFile(
                this,
                "${this.packageName}.aiuta.tryon.compose.fileprovider",
                file,
            )
    } catch (e: IOException) {
        // If an IOException occurs, ignore fallback
    }
    // Return the Uri object, which may be null if an exception was caught.
    return bmpUri
}
