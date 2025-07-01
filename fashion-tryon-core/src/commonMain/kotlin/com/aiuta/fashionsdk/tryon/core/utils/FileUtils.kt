package com.aiuta.fashionsdk.tryon.core.utils

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

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

/**
 * Returns the extension of this file (not including the dot), or an empty string if it doesn't have one.
 */
internal val String.extension: String
    get() = substringAfterLast('.', "")
