package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class ProductInfoSheetViewState(
    val isWishlistAvailable: Boolean = false,
    val isInWishlist: Boolean = false,
)
