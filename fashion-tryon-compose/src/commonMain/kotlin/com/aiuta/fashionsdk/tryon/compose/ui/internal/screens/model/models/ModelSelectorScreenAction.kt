package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models

internal sealed interface ModelSelectorScreenAction {
    data object NavigateBack : ModelSelectorScreenAction
    data object NavigateToImageSelector : ModelSelectorScreenAction
}
