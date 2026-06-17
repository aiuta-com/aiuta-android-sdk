package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.products

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.products.components.ProductRow
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState

internal fun LazyListScope.productsBlock(
    viewState: State<GenerationResultScreenViewState>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
) {
    items(
        items = viewState.value.products,
        key = { product -> product.id },
        contentType = { "product_row" },
    ) { product ->
        ProductRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            productItem = product,
            onClick = {
                eventHandler(GenerationResultScreenEvent.ProductRowClicked(product))
            },
        )
    }
}
