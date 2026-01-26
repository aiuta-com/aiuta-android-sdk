package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig.BellyShape
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig.BraCup
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig.Gender
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig.HipShape

@Immutable
internal data class SizeFitConfigUiModel(
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val gender: Gender = Gender.FEMALE,
    val hipShape: HipShape? = null,
    val bellyShape: BellyShape? = null,
    val braSize: Int? = null,
    val braCup: BraCup? = null,
)
