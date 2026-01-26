package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state

internal sealed interface SizeFitConfigState {
    data object Loading : SizeFitConfigState
    data class Success(val config: SizeFitConfigUiModel) : SizeFitConfigState
}
