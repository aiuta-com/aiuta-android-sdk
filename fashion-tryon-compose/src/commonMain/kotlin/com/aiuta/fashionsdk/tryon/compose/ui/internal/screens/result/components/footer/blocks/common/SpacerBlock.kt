package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

internal fun LazyGridScope.spacerBlock(
    index: Int,
    height: Dp,
) {
    item(
        key = "SPACER_BLOCK_KEY_$index",
        span = { GridItemSpan(maxLineSpan) },
        contentType = "SPACER_BLOCK_KEY_TYPE",
    ) {
        Spacer(Modifier.height(height))
    }
}
