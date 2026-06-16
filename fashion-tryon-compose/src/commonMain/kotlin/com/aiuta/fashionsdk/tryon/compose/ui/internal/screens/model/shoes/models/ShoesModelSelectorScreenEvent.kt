package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models

import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel

internal sealed interface ShoesModelSelectorScreenEvent {

    // App bar / navigation
    data object BackClicked : ShoesModelSelectorScreenEvent

    // Content interactions
    data class GenderSelected(val genderId: String) : ShoesModelSelectorScreenEvent

    data class ModelClicked(val model: TryOnModelUiModel) : ShoesModelSelectorScreenEvent

    // Error
    data object RetryClicked : ShoesModelSelectorScreenEvent
}
