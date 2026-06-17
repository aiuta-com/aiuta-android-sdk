package com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Reason why a try-on operation may be aborted by the backend.
 *
 * Deserialized with [AbortReasonSerializer], which maps any value not listed here
 * (e.g. a new value added on the backend) to [UNKNOWN] instead of throwing, so the
 * SDK keeps working as the API evolves.
 */
@Serializable(with = AbortReasonSerializer::class)
internal enum class AbortReason {
    NO_PEOPLE_DETECTED,
    TOO_MANY_PEOPLE_DETECTED,
    CHILD_DETECTED,
    INSUFFICIENT_TARGET_AREA,
    INTERNAL_RESTRICTION,
    UNKNOWN,
}

internal object AbortReasonSerializer : KSerializer<AbortReason> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AbortReason", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AbortReason) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): AbortReason {
        val raw = decoder.decodeString()
        return AbortReason.entries.firstOrNull { it.name == raw } ?: AbortReason.UNKNOWN
    }
}
