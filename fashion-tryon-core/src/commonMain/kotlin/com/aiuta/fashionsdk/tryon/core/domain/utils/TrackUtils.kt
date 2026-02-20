package com.aiuta.fashionsdk.tryon.core.domain.utils

import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.tryon.core.domain.AiutaTryOnImpl
import com.aiuta.fashionsdk.tryon.core.domain.analytic.sendErrorEvent
import com.aiuta.fashionsdk.tryon.core.domain.analytic.sendPublicTryOnErrorEvent
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnGenerationException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.FlowCollector

internal suspend fun <T> AiutaTryOnImpl.trackException(
    container: ProductGenerationContainer,
    action: suspend () -> T,
): T = try {
    action()
} catch (e: CancellationException) {
    // Just rethrow cancellation, because we don't want to track it
    throw e
} catch (e: AiutaTryOnGenerationException) {
    logger?.e("trackException(): failed with aiuta exception", e)
    analytic.sendErrorEvent(
        container = container,
        exception = e,
    )
    throw e
} catch (e: Exception) {
    // Logging general exception
    logger?.e("trackException(): failed with general exception", e)
    analytic.sendPublicTryOnErrorEvent(
        container = container,
        exception = e,
    )
    throw e
}

internal suspend fun FlowCollector<ProductGenerationStatus>.errorListener(
    statusId: String,
    action: suspend () -> Unit,
) {
    try {
        action()
    } catch (e: Exception) {
        // Fallback with error
        emit(
            ProductGenerationStatus.ErrorGenerationStatus(
                statusId = statusId,
                errorMessage = e.message,
                exception = e,
            ),
        )
        throw e
    }
}
