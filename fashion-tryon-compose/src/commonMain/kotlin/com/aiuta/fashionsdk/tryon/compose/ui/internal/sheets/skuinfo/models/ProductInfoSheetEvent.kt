package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp

internal sealed interface ProductInfoSheetEvent {
    data object WishlistToggled : ProductInfoSheetEvent

    data class ImageClicked(
        val imageUrl: String,
        val imageSize: Size,
        val parentOffset: Offset,
        val cornerRadius: Dp,
    ) : ProductInfoSheetEvent
}
