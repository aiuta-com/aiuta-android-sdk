package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelGenderUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.tryonmodel.TryOnModelUiModel

@Immutable
internal data class ModelSelectorScreenViewState(
    val status: Status = Status.LOADING,
    val genders: List<GenderTabUiModel> = emptyList(),
    val activeGender: TryOnModelGenderUiModel? = null,
    val activeModel: TryOnModelUiModel? = null,
) {
    enum class Status {
        LOADING,
        CONTENT,
        EMPTY_MODELS_ERROR,
        GENERAL_ERROR,
    }
}
