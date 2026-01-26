package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

@Composable
internal fun solveGeneralPriceColor(activeProductItem: ProductItem): Color {
    val theme = LocalTheme.current

    return if (activeProductItem.price?.old?.isNotBlank() == true) {
        theme.color.secondary
    } else {
        theme.color.primary
    }
}

internal fun solveGeneralPriceDecoration(activeProductItem: ProductItem): TextDecoration? = if (activeProductItem.price?.old?.isNotBlank() == true) {
    TextDecoration.LineThrough
} else {
    null
}
