package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal

import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.FashionProductOperationsDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.CreateProductOperationRequest
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.CreateProductOperationResponse
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.GeneratedImage
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.ProductOperation
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.ProductOperationStatus
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnExceptionType
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnGenerationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest

internal class PingOperationSliceImplTest {
    private val image = GeneratedImage(id = "image", url = "https://example.com/image.jpg", type = AiutaFileType.AIUTA)

    /**
     * Answers polls with [statuses] in order (null: the request fails like a
     * network error) and records the virtual time of each poll
     */
    private class FakeOperationsDataSource(
        private val scope: TestScope,
        private val statuses: List<ProductOperationStatus?>,
        private val images: List<GeneratedImage>,
    ) : FashionProductOperationsDataSource {
        val pollTimes = mutableListOf<Long>()

        override suspend fun createProductOperation(request: CreateProductOperationRequest): CreateProductOperationResponse = error("Not used")

        override suspend fun getProductOperation(operationId: String): ProductOperation {
            pollTimes += scope.currentTime
            val status = statuses[pollTimes.size - 1] ?: throw IllegalStateException("Network error")
            return ProductOperation(
                id = operationId,
                status = status,
                generatedImages = if (status == ProductOperationStatus.SUCCESS) images else emptyList(),
            )
        }
    }

    private fun TestScope.slice(
        statuses: List<ProductOperationStatus?>,
        delays: List<Long>,
    ): Pair<PingOperationSliceImpl, FakeOperationsDataSource> {
        val dataSource = FakeOperationsDataSource(this, statuses, listOf(image))
        return PingOperationSliceImpl(dataSource) { delays.asSequence() } to dataSource
    }

    @Test
    fun waitsEachDelayBeforePollingAndReturnsResultOnSuccess() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(ProductOperationStatus.CREATED, ProductOperationStatus.IN_PROGRESS, ProductOperationStatus.SUCCESS),
            delays = listOf(1000L, 300L, 300L, 300L),
        )

        val result = slice.operationPing("operation")

        assertEquals(listOf(image), result)
        // No poll right away: the first one comes after the first delay
        assertEquals(listOf(1000L, 1300L, 1600L), dataSource.pollTimes)
    }

    @Test
    fun timesOutWhenDelaysRunOutBeforeOperationSettles() = runTest {
        val (slice, dataSource) = slice(
            statuses = List(3) { ProductOperationStatus.IN_PROGRESS },
            delays = listOf(500L, 500L),
        )

        val exception = assertFailsWith<AiutaTryOnGenerationException> { slice.operationPing("operation") }

        assertEquals(AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED, exception.type)
        assertEquals(listOf(500L, 1000L), dataSource.pollTimes)
    }

    @Test
    fun failedOperationStopsPolling() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(ProductOperationStatus.IN_PROGRESS, ProductOperationStatus.FAILED),
            delays = List(10) { 1000L },
        )

        val exception = assertFailsWith<AiutaTryOnGenerationException> { slice.operationPing("operation") }

        assertEquals(AiutaTryOnExceptionType.OPERATION_FAILED, exception.type)
        assertEquals(2, dataSource.pollTimes.size)
    }

    @Test
    fun abortedOperationStopsPolling() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(ProductOperationStatus.ABORTED),
            delays = List(10) { 1000L },
        )

        val exception = assertFailsWith<AiutaTryOnGenerationException> { slice.operationPing("operation") }

        assertEquals(AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED, exception.type)
        assertEquals(1, dataSource.pollTimes.size)
    }

    @Test
    fun retryAfterFailedPollPollsRightAwayAndContinuesTheSchedule() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(ProductOperationStatus.IN_PROGRESS, null, ProductOperationStatus.IN_PROGRESS, ProductOperationStatus.SUCCESS),
            delays = listOf(1000L, 1000L, 500L, 500L),
        )

        assertFailsWith<IllegalStateException> { slice.operationPing("operation") }
        // The retry policy waits its retry delay, then pings again
        delay(1000L)
        val result = slice.operationPing("operation")

        assertEquals(listOf(image), result)
        // Retry polls at 3000 without an extra wait, then the next delay (500)
        assertEquals(listOf(1000L, 2000L, 3000L, 3500L), dataSource.pollTimes)
    }

    @Test
    fun retryDoesNotRestartAFiniteScheduleSoItStillTimesOut() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(ProductOperationStatus.IN_PROGRESS, null, ProductOperationStatus.IN_PROGRESS),
            delays = listOf(1000L, 1000L),
        )

        assertFailsWith<IllegalStateException> { slice.operationPing("operation") }
        val exception = assertFailsWith<AiutaTryOnGenerationException> { slice.operationPing("operation") }

        assertEquals(AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED, exception.type)
        assertEquals(3, dataSource.pollTimes.size)
    }

    @Test
    fun newOperationStartsItsOwnScheduleWithAWait() = runTest {
        val (slice, dataSource) = slice(
            statuses = listOf(null, ProductOperationStatus.SUCCESS),
            delays = listOf(1000L, 1000L),
        )

        assertFailsWith<IllegalStateException> { slice.operationPing("first") }
        slice.operationPing("second")

        // The other operation doesn't resume the failed one: it waits first
        assertEquals(listOf(1000L, 2000L), dataSource.pollTimes)
    }
}
