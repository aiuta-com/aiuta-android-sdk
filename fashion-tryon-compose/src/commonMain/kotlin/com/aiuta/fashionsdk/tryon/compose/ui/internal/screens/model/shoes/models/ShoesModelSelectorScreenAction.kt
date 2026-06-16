package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models

internal sealed interface ShoesModelSelectorScreenAction {
    data object NavigateBack : ShoesModelSelectorScreenAction
    data object NavigateToImageSelector : ShoesModelSelectorScreenAction
}
