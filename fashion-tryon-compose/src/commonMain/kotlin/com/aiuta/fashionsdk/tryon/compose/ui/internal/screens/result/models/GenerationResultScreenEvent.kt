package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel

internal sealed interface GenerationResultScreenEvent {

    // Photo
    data class PhotoClicked(
        val sessionImage: SessionImageUIModel,
        val imageSize: Size,
        val parentOffset: Offset,
        val cornerRadius: Dp,
    ) : GenerationResultScreenEvent

    // Photo overlays
    data class WishlistToggled(
        val sessionImage: SessionImageUIModel,
        val isAdding: Boolean,
    ) : GenerationResultScreenEvent

    data class LikeClicked(val sessionImage: SessionImageUIModel) : GenerationResultScreenEvent
    data class DislikeClicked(val sessionImage: SessionImageUIModel) : GenerationResultScreenEvent
    data class ChangePhotoClicked(val sessionImage: SessionImageUIModel) : GenerationResultScreenEvent

    // Feedback bottom sheet closed -> show the "thanks" overlay
    data object FeedbackProvidedViaSheet : GenerationResultScreenEvent

    // Cart / products
    data object AddToCartClicked : GenerationResultScreenEvent
    data class ProductRowClicked(val product: ProductItem) : GenerationResultScreenEvent

    // Disclaimer
    data object FitDisclaimerClicked : GenerationResultScreenEvent
}
