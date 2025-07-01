package com.aiuta.fashionsdk.io.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

/**
 * [generateFile] returns generated [File] specified by [path] and [fileName]
 *
 * @param path - directory in which a new file will be generated
 * @param fileName - name of a file that is about to be generated
 *
 * @return generated file or [null] if file could not be generated
 * due to caught [SecurityException] or inability of Android OS to generate file
 */
internal fun generateFile(
    path: String,
    fileName: String = generateFileName(),
): File? {
    var file: File? = null

    try {
        val root = File(path)

        var dirExists = true
        if (!root.exists()) {
            /*
             * It is possible that some dirs were created but not all of them
             * In this case [dirExists will return false]
             * */
            dirExists = root.mkdirs()
        }

        if (dirExists) {
            file = File(root, fileName)
        }
    } catch (e: SecurityException) {
        file = null
    }

    return file
}

/**
 * [File.writeByteArray] is an extension function on [File] that writes [ByteArray] to given file
 *
 * This is a suspend function which operates on [Dispatchers.IO] context
 * to omit deadlocks if they happen on [Dispatchers.Main] thread
 *
 * @param byteArray - byteArray that must be written into given File
 *
 * @return boolean, which is true if byteArray was successfully written,
 * and false if exception was thrown during write operation
 */
internal suspend inline fun File.writeByteArray(
    byteArray: ByteArray,
    crossinline onExceptionCaughtAction: (exceptionMessage: String?) -> Unit = {},
): Boolean = withContext(Dispatchers.IO) {
    var isByteArraySuccessfullyWritten: Boolean = true
    var fileOutputStream: FileOutputStream? = null

    try {
        fileOutputStream =
            FileOutputStream(this@writeByteArray).apply {
                write(byteArray)
                flush()
            }
    } catch (e: FileNotFoundException) {
        onExceptionCaughtAction.invoke(e.message)

        isByteArraySuccessfullyWritten = false
    } catch (e: SecurityException) {
        onExceptionCaughtAction.invoke(e.message)

        isByteArraySuccessfullyWritten = false
    } catch (e: IOException) {
        onExceptionCaughtAction.invoke(e.message)

        isByteArraySuccessfullyWritten = false
    } finally {
        fileOutputStream?.close()
    }

    return@withContext isByteArraySuccessfullyWritten
}

/**
 * Function for generating file name, depends on current time
 *
 * @param fileDateFormat which will use for style naming
 * @param fileExtension is extension of output file
 *
 * @return [String] with name of file. By default, will be `2015-12-31-12-30-123.jpeg`, for example
 */
@OptIn(ExperimentalTime::class)
internal fun generateFileName(
    fileNameAdditional: String = "",
    fileExtension: String = "jpeg",
): String {
    val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val datePrefix =
        with(dateTime) {
            "$year-${month.number}-$day-$hour-$minute-$second-$nanosecond"
        }
    return "$datePrefix$fileNameAdditional.$fileExtension"
}
