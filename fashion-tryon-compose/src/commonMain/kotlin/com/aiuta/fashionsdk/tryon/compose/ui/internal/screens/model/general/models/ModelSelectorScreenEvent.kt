package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models

import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel

internal sealed interface ModelSelectorScreenEvent {

    // App bar / navigation
    data object BackClicked : ModelSelectorScreenEvent

    // Content interactions
    data class GenderSelected(val genderId: String) : ModelSelectorScreenEvent

    data class ActiveModelChanged(val model: TryOnModelUiModel?) : ModelSelectorScreenEvent

    data object TryOnClicked : ModelSelectorScreenEvent

    // Error
    data object RetryClicked : ModelSelectorScreenEvent
}
