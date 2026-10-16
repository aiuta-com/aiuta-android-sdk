package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal

import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.FashionProductOperationsDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.GeneratedImage
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.ProductOperationStatus
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.PingOperationSlice
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnExceptionType
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnGenerationException
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.toPublic
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils.defaultGenerationDelaySequence
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class PingOperationSliceImpl(
    private val productOperationsDataSource: FashionProductOperationsDataSource,
    private val delaySequenceFactory: suspend () -> Sequence<Long> = { defaultGenerationDelaySequence() },
) : PingOperationSlice {
    private val mutex = Mutex()

    // Schedule of the last polled operation, kept until it succeeds
    private var pollingOperationId: String? = null
    private var pollingDelays: Iterator<Long>? = null

    override suspend fun operationPing(operationId: String): List<GeneratedImage> = mutex.withLock {
        // The retry policy calls ping again after a failed poll and has already
        // waited its retry delay, so the retry polls right away and goes on
        // with the same schedule instead of starting it over
        val resumedDelays = pollingDelays?.takeIf { pollingOperationId == operationId }
        val delays = resumedDelays ?: delaySequenceFactory().iterator()
        pollingOperationId = operationId
        pollingDelays = delays

        poll(
            operationId = operationId,
            delays = delays,
            waitBeforeFirstPoll = resumedDelays == null,
        ).also {
            pollingOperationId = null
            pollingDelays = null
        }
    }

    private suspend fun poll(
        operationId: String,
        delays: Iterator<Long>,
        waitBeforeFirstPoll: Boolean,
    ): List<GeneratedImage> {
        var shouldWait = waitBeforeFirstPoll
        while (true) {
            // Each delay is waited before a poll: right after the operation is
            // created it is never ready, so there's nothing to ask yet
            if (shouldWait) {
                if (!delays.hasNext()) {
                    // We reach time limit
                    throw AiutaTryOnGenerationException(AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED)
                }
                delay(delays.next())
            }
            shouldWait = true

            val operation = productOperationsDataSource.getProductOperation(operationId)

            when (operation.status) {
                ProductOperationStatus.FAILED -> {
                    throw AiutaTryOnGenerationException(
                        type = AiutaTryOnExceptionType.OPERATION_FAILED,
                        message = operation.error,
                    )
                }

                ProductOperationStatus.ABORTED -> {
                    throw AiutaTryOnGenerationException(
                        type = AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED,
                        message = operation.error,
                        abortReason = operation.abortReason?.toPublic(),
                    )
                }

                ProductOperationStatus.CREATED, ProductOperationStatus.IN_PROGRESS -> {
                    // Just wait result
                }

                ProductOperationStatus.SUCCESS -> {
                    return operation.generatedImages
                }
            }
        }
    }
}
