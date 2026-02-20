package com.aiuta.fashionsdk.internal.analytics.internal.updater

import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.logger.i
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withTimeout

internal abstract class BaseUpdater(
    private val logger: AiutaLogger?,
) {
    /**
     * Return version of [retryAction], but can throw [CancellationException]
     */
    suspend fun <T> retryAction(
        maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
        block: suspend () -> T,
    ): Result<T> = suspendRunCatching {
        for (attempt in 1..maxAttempts) {
            // Check if active
            currentCoroutineContext().ensureActive()

            // Try to update
            try {
                // Execute action
                return@suspendRunCatching block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger?.e("retryAction($attempt): Failed to execute block - $e", e)
            }

            // Wait for new attempt if retrying
            delay(UPDATE_DELAY)
        }

        error("retryAction(): Failed to get data after $maxAttempts attempts")
    }

    suspend fun <T> retryActionWithTimeOut(
        timeout: Duration = DEFAULT_TIMEOUT,
        maxAttempts: Int = DEFAULT_MAX_ATTEMPTS,
        block: suspend () -> T,
    ): Result<T> = withTimeout(
        timeout = timeout,
    ) {
        retryAction(
            maxAttempts = maxAttempts,
            block = block,
        )
    }

    private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (cancellationException: kotlin.coroutines.cancellation.CancellationException) {
        throw cancellationException
    } catch (exception: Exception) {
        logger?.i(
            message = "Failed to evaluate a suspendRunCatchingBlock. Returning failure UIResult - $exception",
            throwable = exception,
        )
        Result.failure(exception)
    }

    private companion object {
        const val UPDATE_DELAY = 3000L
        const val DEFAULT_MAX_ATTEMPTS = 5
        val DEFAULT_TIMEOUT = 1.minutes
    }
}
