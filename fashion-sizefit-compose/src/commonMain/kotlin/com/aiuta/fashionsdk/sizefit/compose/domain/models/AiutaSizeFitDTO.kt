package com.aiuta.fashionsdk.sizefit.compose.domain.models

import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig
import kotlinx.serialization.Serializable

@Serializable
internal class AiutaSizeFitConfigDTO(
    public val age: Int,
    public val height: Int,
    public val weight: Int,
    public val gender: AiutaSizeFitConfig.Gender,
    public val hipShape: AiutaSizeFitConfig.HipShape? = null,
    public val bellyShape: AiutaSizeFitConfig.BellyShape? = null,
    public val braSize: Int? = null,
    public val braCup: AiutaSizeFitConfig.BraCup? = null,
)

internal fun AiutaSizeFitConfigDTO.toUiState(): SizeFitConfigUiModel = SizeFitConfigUiModel(
    age = age,
    height = height,
    weight = weight,
    gender = gender,
    hipShape = hipShape,
    bellyShape = bellyShape,
    braSize = braSize,
    braCup = braCup,
)

internal fun SizeFitConfigUiModel.toDTO(): AiutaSizeFitConfigDTO {
    val errorMessage: (String) -> String = { prop ->
        "ERROR: Fail to convert SizeFitConfigUiModel to AiutaSizeFitConfigDTO, because $prop is null"
    }

    return AiutaSizeFitConfigDTO(
        age = checkNotNull(age) { errorMessage("age") },
        height = checkNotNull(height) { errorMessage("height") },
        weight = checkNotNull(weight) { errorMessage("weight") },
        gender = gender,
        hipShape = hipShape,
        bellyShape = bellyShape,
        braSize = braSize,
        braCup = braCup,
    )
}

internal fun SizeFitConfigUiModel.toCore(): AiutaSizeFitConfig {
    val errorMessage: (String) -> String = { prop ->
        "ERROR: Fail to convert SizeFitConfigUiModel to AiutaSizeFitConfigDTO, because $prop is null"
    }

    return AiutaSizeFitConfig(
        age = checkNotNull(age) { errorMessage("age") },
        height = checkNotNull(height) { errorMessage("height") },
        weight = checkNotNull(weight) { errorMessage("weight") },
        gender = gender,
        hipShape = hipShape,
        bellyShape = bellyShape,
        braSize = braSize,
        braCup = braCup,
    )
}
