package com.aiuta.fashionsdk.logger

import android.util.Log

internal actual fun nativeLog(
    priority: AiutaLogger.Level,
    tag: String?,
    throwable: Throwable?,
    message: String,
) {
    when (priority) {
        AiutaLogger.Level.VERBOSE -> Log.v(tag, message, throwable)
        AiutaLogger.Level.DEBUG -> Log.d(tag, message, throwable)
        AiutaLogger.Level.INFO -> Log.i(tag, message, throwable)
        AiutaLogger.Level.WARN -> Log.w(tag, message, throwable)
        AiutaLogger.Level.ERROR -> Log.e(tag, message, throwable)
    }
}
