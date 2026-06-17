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
    private val delaySequenceFactory: () -> Sequence<Long> = { defaultGenerationDelaySequence() },
) : PingOperationSlice {
    private val mutex = Mutex()

    override suspend fun operationPing(operationId: String): List<GeneratedImage> = mutex.withLock {
        ping(
            operationId = operationId,
            delaySequenceIterator = delaySequenceFactory().iterator(),
        )
    }

    private suspend fun ping(
        operationId: String,
        delaySequenceIterator: Iterator<Long>,
    ): List<GeneratedImage> {
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

        if (delaySequenceIterator.hasNext()) {
            delay(delaySequenceIterator.next())
        } else {
            // We reach time limit
            throw AiutaTryOnGenerationException(AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED)
        }

        return ping(
            operationId = operationId,
            delaySequenceIterator = delaySequenceIterator,
        )
    }
}
