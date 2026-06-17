package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models

import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

internal sealed interface GenerationResultScreenAction {
    data object NavigateBack : GenerationResultScreenAction

    data class ShowFeedbackSheet(val productIds: List<String>) : GenerationResultScreenAction
    data object ShowFitDisclaimerSheet : GenerationResultScreenAction
    data class ShowChangePhotoSheet(val hasMultipleOperations: Boolean) : GenerationResultScreenAction
    data class ShowProductInfoSheet(val product: ProductItem) : GenerationResultScreenAction
}
