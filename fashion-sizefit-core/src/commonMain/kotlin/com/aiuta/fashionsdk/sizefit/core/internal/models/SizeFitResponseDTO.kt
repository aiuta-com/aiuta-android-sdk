package com.aiuta.fashionsdk.sizefit.core.internal.models

import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SizeFitResponseDTO(
    @SerialName("bodyMeasurements")
    val bodyMeasurements: List<BodyMeasurement>,
    @SerialName("recommendedSizeName")
    val recommendedSizeName: String,
    @SerialName("sizes")
    val sizes: List<Size>,
) {
    @Serializable
    data class BodyMeasurement(
        @SerialName("type")
        val type: String,
        @SerialName("value")
        val value: Int,
    )

    @Serializable
    data class Size(
        @SerialName("measurements")
        val measurements: List<Measurement>,
        @SerialName("name")
        val name: String,
    ) {
        @Serializable
        data class Measurement(
            @SerialName("dist")
            val dist: Int,
            @SerialName("fitRatio")
            val fitRatio: Int,
            @SerialName("max")
            val max: Int,
            @SerialName("min")
            val min: Int,
            @SerialName("type")
            val type: String,
        )
    }
}

// Mappers
internal fun SizeFitResponseDTO.fromDTO(): AiutaSizeFitRecommendation = AiutaSizeFitRecommendation(
    bodyMeasurements = bodyMeasurements.map { it.fromDTO() },
    recommendedSizeName = recommendedSizeName,
    sizes = sizes.map { it.fromDTO() },
)

internal fun SizeFitResponseDTO.BodyMeasurement.fromDTO(): AiutaSizeFitRecommendation.BodyMeasurement = AiutaSizeFitRecommendation.BodyMeasurement(
    type = type,
    value = value,
)

internal fun SizeFitResponseDTO.Size.fromDTO(): AiutaSizeFitRecommendation.Size = AiutaSizeFitRecommendation.Size(
    measurements = measurements.map { it.fromDTO() },
    name = name,
)

internal fun SizeFitResponseDTO.Size.Measurement.fromDTO(): AiutaSizeFitRecommendation.Size.Measurement = AiutaSizeFitRecommendation.Size.Measurement(
    dist = dist,
    fitRatio = fitRatio,
    max = max,
    min = min,
    type = type,
)
