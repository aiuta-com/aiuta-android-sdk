package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception

import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.AbortReason

/**
 * Public reason why a try-on operation was aborted.
 *
 * Carried by [AiutaTryOnGenerationException] when its type is
 * [AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED]. [UNKNOWN] represents a reason that
 * the current SDK version does not recognize (e.g. a value newly introduced on the
 * backend); consumers should treat it as a generic abort.
 */
public enum class AiutaTryOnAbortReason {
    NO_PEOPLE_DETECTED,
    TOO_MANY_PEOPLE_DETECTED,
    CHILD_DETECTED,
    INSUFFICIENT_TARGET_AREA,
    INTERNAL_RESTRICTION,
    UNKNOWN,
}

internal fun AbortReason.toPublic(): AiutaTryOnAbortReason = when (this) {
    AbortReason.NO_PEOPLE_DETECTED -> AiutaTryOnAbortReason.NO_PEOPLE_DETECTED
    AbortReason.TOO_MANY_PEOPLE_DETECTED -> AiutaTryOnAbortReason.TOO_MANY_PEOPLE_DETECTED
    AbortReason.CHILD_DETECTED -> AiutaTryOnAbortReason.CHILD_DETECTED
    AbortReason.INSUFFICIENT_TARGET_AREA -> AiutaTryOnAbortReason.INSUFFICIENT_TARGET_AREA
    AbortReason.INTERNAL_RESTRICTION -> AiutaTryOnAbortReason.INTERNAL_RESTRICTION
    AbortReason.UNKNOWN -> AiutaTryOnAbortReason.UNKNOWN
}
