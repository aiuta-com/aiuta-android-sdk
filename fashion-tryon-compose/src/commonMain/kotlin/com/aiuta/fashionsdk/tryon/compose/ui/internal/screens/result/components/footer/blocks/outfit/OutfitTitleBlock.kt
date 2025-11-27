package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.outfit

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

internal fun LazyGridScope.outfitTitleBlock(
    modifier: Modifier = Modifier,
) {
    item(
        key = "OUTFIT_TITLE_KEY",
        span = { GridItemSpan(maxLineSpan) },
        contentType = "OUTFIT_TITLE_TYPE",
    ) {
        val theme = LocalTheme.current
        val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()

        Text(
            modifier = modifier,
            text = tryOnFeature.strings.outfitTitle,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
        )
    }
}
