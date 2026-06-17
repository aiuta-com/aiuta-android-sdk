package com.aiuta.fashionsdk.tryon.core.domain.analytic

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsTryOnAbortedReasonType
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception.AiutaTryOnAbortReason

/**
 * Maps a public [AiutaTryOnAbortReason] to its analytics counterpart. An unknown or
 * missing reason falls back to [AiutaAnalyticsTryOnAbortedReasonType.OPERATION_ABORTED].
 */
internal fun AiutaTryOnAbortReason?.toAnalyticsReason(): AiutaAnalyticsTryOnAbortedReasonType = when (this) {
    AiutaTryOnAbortReason.NO_PEOPLE_DETECTED -> AiutaAnalyticsTryOnAbortedReasonType.NO_PEOPLE_DETECTED
    AiutaTryOnAbortReason.TOO_MANY_PEOPLE_DETECTED -> AiutaAnalyticsTryOnAbortedReasonType.TOO_MANY_PEOPLE_DETECTED
    AiutaTryOnAbortReason.CHILD_DETECTED -> AiutaAnalyticsTryOnAbortedReasonType.CHILD_DETECTED
    AiutaTryOnAbortReason.INSUFFICIENT_TARGET_AREA -> AiutaAnalyticsTryOnAbortedReasonType.INSUFFICIENT_TARGET_AREA
    AiutaTryOnAbortReason.INTERNAL_RESTRICTION -> AiutaAnalyticsTryOnAbortedReasonType.INTERNAL_RESTRICTION
    AiutaTryOnAbortReason.UNKNOWN, null -> AiutaAnalyticsTryOnAbortedReasonType.OPERATION_ABORTED
}
