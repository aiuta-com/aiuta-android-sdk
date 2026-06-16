package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.ShoesModelGenderUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models.GenderTabUiModel

@Immutable
internal data class ShoesModelSelectorScreenViewState(
    val status: Status = Status.LOADING,
    val genders: List<GenderTabUiModel> = emptyList(),
    val activeGender: ShoesModelGenderUiModel? = null,
) {
    enum class Status {
        LOADING,
        CONTENT,
        EMPTY_MODELS_ERROR,
        GENERAL_ERROR,
    }
}
