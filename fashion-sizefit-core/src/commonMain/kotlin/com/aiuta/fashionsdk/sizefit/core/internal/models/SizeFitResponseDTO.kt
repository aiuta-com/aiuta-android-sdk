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
            val dist: Float,
            @SerialName("fitRatio")
            val fitRatio: Float,
            @SerialName("max")
            val max: Int,
            @SerialName("min")
            val min: Int,
            @SerialName("type")
            val type: MeasurementType,
        ) {
            @Serializable
            public enum class MeasurementType {
                @SerialName("chest_c")
                CHEST_C,

                @SerialName("waist_c")
                WAIST_C,

                @SerialName("hip_c")
                HIP_C,

                @SerialName("bmi")
                BMI,

                @SerialName("inseam")
                INSEAM,

                @SerialName("cup")
                CUP,

                @SerialName("bra_underbust")
                BRA_UNDERBUST,

                @SerialName("over_bust")
                OVER_BUST,

                @SerialName("under_bust")
                UNDER_BUST,
            }
        }
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
    type = type.fromDTO(),
)

internal fun SizeFitResponseDTO.Size.Measurement.MeasurementType.fromDTO(): AiutaSizeFitRecommendation.Size.Measurement.MeasurementType = when (this) {
    SizeFitResponseDTO.Size.Measurement.MeasurementType.CHEST_C -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.CHEST_C
    SizeFitResponseDTO.Size.Measurement.MeasurementType.WAIST_C -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.WAIST_C
    SizeFitResponseDTO.Size.Measurement.MeasurementType.HIP_C -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.HIP_C
    SizeFitResponseDTO.Size.Measurement.MeasurementType.BMI -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.BMI
    SizeFitResponseDTO.Size.Measurement.MeasurementType.INSEAM -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.INSEAM
    SizeFitResponseDTO.Size.Measurement.MeasurementType.CUP -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.CUP
    SizeFitResponseDTO.Size.Measurement.MeasurementType.BRA_UNDERBUST -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.BRA_UNDERBUST
    SizeFitResponseDTO.Size.Measurement.MeasurementType.OVER_BUST -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.OVER_BUST
    SizeFitResponseDTO.Size.Measurement.MeasurementType.UNDER_BUST -> AiutaSizeFitRecommendation.Size.Measurement.MeasurementType.UNDER_BUST
}
