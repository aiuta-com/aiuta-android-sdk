package com.aiuta.fashionsdk.tryon.core.domain.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnAbortedReasonType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnErrorType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnEvent
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnEventType
import com.aiuta.fashionsdk.internal.analytic.InternalAiutaAnalytic
import com.aiuta.fashionsdk.network.exceptions.FashionIOException
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationContainer
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnExceptionType
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnGenerationException

internal fun InternalAiutaAnalytic.sendStartTryOnEvent(container: ProductGenerationContainer) {
    sendEvent(
        event = AiutaAnalyticsTryOnEvent(
            event = AiutaAnalyticsTryOnEventType.TRY_ON_STARTED,
            pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
            productId = container.productId,
        ),
    )
}

internal fun InternalAiutaAnalytic.sendPublicTryOnErrorEvent(
    container: ProductGenerationContainer,
    exception: Exception,
) {
    sendEvent(
        event = AiutaAnalyticsTryOnEvent(
            event = AiutaAnalyticsTryOnEventType.TRY_ON_ERROR,
            errorType = when (exception) {
                is AiutaTryOnGenerationException -> when (exception.type) {
                    AiutaTryOnExceptionType.PREPARE_PHOTO_FAILED -> AiutaAnalyticsTryOnErrorType.PREPARE_PHOTO_FAILED
                    AiutaTryOnExceptionType.UPLOAD_PHOTO_FAILED -> AiutaAnalyticsTryOnErrorType.UPLOAD_PHOTO_FAILED
                    AiutaTryOnExceptionType.START_OPERATION_FAILED -> AiutaAnalyticsTryOnErrorType.START_OPERATION_FAILED
                    AiutaTryOnExceptionType.OPERATION_FAILED -> AiutaAnalyticsTryOnErrorType.OPERATION_FAILED
                    AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED -> AiutaAnalyticsTryOnErrorType.OPERATION_ABORTED
                    AiutaTryOnExceptionType.OPERATION_TIMEOUT_FAILED -> AiutaAnalyticsTryOnErrorType.OPERATION_TIMEOUT
                    AiutaTryOnExceptionType.OPERATION_EMPTY_RESULTS_FAILED -> AiutaAnalyticsTryOnErrorType.OPERATION_EMPTY_RESULTS
                    AiutaTryOnExceptionType.DOWNLOAD_RESULT_FAILED -> AiutaAnalyticsTryOnErrorType.DOWNLOAD_RESULT_FAILED
                }

                is FashionIOException -> when (exception.errorCode) {
                    // Unauthorized
                    401 -> AiutaAnalyticsTryOnErrorType.AUTHORIZATION_FAILED
                    else -> AiutaAnalyticsTryOnErrorType.START_OPERATION_FAILED
                }

                else -> AiutaAnalyticsTryOnErrorType.INTERNAL_SDK_ERROR
            },
            errorMessage = exception.message,
            pageId = AiutaAnalyticsPageId.LOADING,
            productId = container.productId,
        ),
    )
}

internal fun InternalAiutaAnalytic.sendPublicTryOnAbortedErrorEvent(
    container: ProductGenerationContainer,
) {
    sendEvent(
        event = AiutaAnalyticsTryOnEvent(
            event = AiutaAnalyticsTryOnEventType.TRY_ON_ABORTED,
            abortReason = AiutaAnalyticsTryOnAbortedReasonType.OPERATION_ABORTED,
            pageId = AiutaAnalyticsPageId.LOADING,
            productId = container.productId,
        ),
    )
}

internal fun InternalAiutaAnalytic.sendErrorEvent(
    container: ProductGenerationContainer,
    exception: AiutaTryOnGenerationException,
) {
    when (exception.type) {
        AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED -> {
            sendPublicTryOnAbortedErrorEvent(
                container = container,
            )
        }

        else -> {
            sendPublicTryOnErrorEvent(container = container, exception = exception)
        }
    }
}

internal fun InternalAiutaAnalytic.sendTryOnPhotoUploadedEvent(container: ProductGenerationContainer) {
    sendEvent(
        event =
        AiutaAnalyticsTryOnEvent(
            event = AiutaAnalyticsTryOnEventType.PHOTO_UPLOADED,
            pageId = AiutaAnalyticsPageId.LOADING,
            productId = container.productId,
        ),
    )
}
