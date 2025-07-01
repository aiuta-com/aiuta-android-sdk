package com.aiuta.fashionsdk.tryon.compose.domain.internal.time

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.time.TimeDataSource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class TimeSaver(
    private val timeDataSource: TimeDataSource,
) {
    @OptIn(ExperimentalTime::class)
    fun getCurrentTime(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    suspend fun saveCurrentTime(key: String) {
        val currentTime = getCurrentTime()
        timeDataSource.insertOrUpdateTimestamp(
            key = key,
            timestamp = currentTime.toString(),
        )
    }

    suspend fun getLastSavedTime(key: String): LocalDateTime? {
        val lastSavedTime = timeDataSource.getTimestamp(key)?.timestamp
        return try {
            lastSavedTime?.let { LocalDateTime.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        fun getInstance(platformContext: AiutaPlatformContext): TimeSaver = TimeSaver(
            timeDataSource = TimeDataSource.getInstance(platformContext),
        )
    }
}
