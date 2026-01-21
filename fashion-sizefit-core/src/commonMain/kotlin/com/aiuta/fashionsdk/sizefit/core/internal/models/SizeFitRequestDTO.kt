package com.aiuta.fashionsdk.sizefit.core.internal.models

import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SizeFitRequestDTO(
    @SerialName("age")
    val age: Int,
    @SerialName("bellyShape")
    val bellyShape: BellyShape? = null,
    @SerialName("braCup")
    val braCup: BraCup? = null,
    @SerialName("braSize")
    val braSize: Int? = null,
    @SerialName("code")
    val code: String,
    @SerialName("gender")
    val gender: Gender,
    @SerialName("height")
    val height: Int,
    @SerialName("hipShape")
    val hipShape: HipShape? = null,
    @SerialName("weight")
    val weight: Int,
) {
    @Serializable
    enum class Gender {
        @SerialName("Female")
        FEMALE,

        @SerialName("Male")
        MALE,
    }

    @Serializable
    enum class HipShape {
        @SerialName("Slim")
        SLIM,

        @SerialName("Normal")
        NORMAL,

        @SerialName("Curvy")
        CURVY,
    }

    @Serializable
    enum class BellyShape {
        @SerialName("Flat")
        FLAT,

        @SerialName("Normal")
        NORMAL,

        @SerialName("Curvy")
        CURVY,
    }

    @Serializable
    enum class BraCup {
        @SerialName("AA")
        AA,

        @SerialName("A")
        A,

        @SerialName("B")
        B,

        @SerialName("C")
        C,

        @SerialName("D")
        D,

        @SerialName("DD")
        DD,

        @SerialName("E")
        E,

        @SerialName("F")
        F,

        @SerialName("G")
        G,
    }
}

// Mappers
internal fun AiutaSizeFitConfig.toDTO(code: String): SizeFitRequestDTO = SizeFitRequestDTO(
    age = age,
    bellyShape = bellyShape?.toDTO(),
    braCup = braCup?.toDTO(),
    braSize = braSize,
    code = code,
    gender = gender.toDTO(),
    height = height,
    hipShape = hipShape?.toDTO(),
    weight = weight,
)

internal fun AiutaSizeFitConfig.BellyShape.toDTO(): SizeFitRequestDTO.BellyShape = when (this) {
    AiutaSizeFitConfig.BellyShape.FLAT -> SizeFitRequestDTO.BellyShape.FLAT
    AiutaSizeFitConfig.BellyShape.NORMAL -> SizeFitRequestDTO.BellyShape.NORMAL
    AiutaSizeFitConfig.BellyShape.CURVY -> SizeFitRequestDTO.BellyShape.CURVY
}

internal fun AiutaSizeFitConfig.BraCup.toDTO(): SizeFitRequestDTO.BraCup = when (this) {
    AiutaSizeFitConfig.BraCup.AA -> SizeFitRequestDTO.BraCup.AA
    AiutaSizeFitConfig.BraCup.A -> SizeFitRequestDTO.BraCup.A
    AiutaSizeFitConfig.BraCup.B -> SizeFitRequestDTO.BraCup.B
    AiutaSizeFitConfig.BraCup.C -> SizeFitRequestDTO.BraCup.C
    AiutaSizeFitConfig.BraCup.D -> SizeFitRequestDTO.BraCup.D
    AiutaSizeFitConfig.BraCup.DD -> SizeFitRequestDTO.BraCup.DD
    AiutaSizeFitConfig.BraCup.E -> SizeFitRequestDTO.BraCup.E
    AiutaSizeFitConfig.BraCup.F -> SizeFitRequestDTO.BraCup.F
    AiutaSizeFitConfig.BraCup.G -> SizeFitRequestDTO.BraCup.G
}

internal fun AiutaSizeFitConfig.Gender.toDTO(): SizeFitRequestDTO.Gender = when (this) {
    AiutaSizeFitConfig.Gender.FEMALE -> SizeFitRequestDTO.Gender.FEMALE
    AiutaSizeFitConfig.Gender.MALE -> SizeFitRequestDTO.Gender.MALE
}

internal fun AiutaSizeFitConfig.HipShape.toDTO(): SizeFitRequestDTO.HipShape = when (this) {
    AiutaSizeFitConfig.HipShape.SLIM -> SizeFitRequestDTO.HipShape.SLIM
    AiutaSizeFitConfig.HipShape.NORMAL -> SizeFitRequestDTO.HipShape.NORMAL
    AiutaSizeFitConfig.HipShape.CURVY -> SizeFitRequestDTO.HipShape.CURVY
}
