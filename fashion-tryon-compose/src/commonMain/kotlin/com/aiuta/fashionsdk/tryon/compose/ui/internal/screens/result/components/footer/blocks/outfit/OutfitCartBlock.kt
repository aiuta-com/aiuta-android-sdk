package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.outfit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.outfit.AiutaTryOnCartOutfitFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddOutfitToCart
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController

internal fun LazyGridScope.outfitCartBlock(
    modifier: Modifier = Modifier,
) {
    item(
        key = "OUTFIT_CART_KEY",
        span = { GridItemSpan(maxLineSpan) },
        contentType = "OUTFIT_CART_TYPE",
    ) {
        val controller = LocalController.current
        val theme = LocalTheme.current
        val outfitFeature = provideFeature<AiutaTryOnCartOutfitFeature>()

        outfitFeature?.let {
            Column(modifier = modifier) {
                Spacer(Modifier.height(40.dp))

                FashionButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = outfitFeature.strings.addToCartOutfit,
                    style = FashionButtonStyles.primaryStyle(theme),
                    size = FashionButtonSizes.lSize(),
                    onClick = {
                        controller.clickAddOutfitToCart(
                            pageId = AiutaAnalyticsPageId.RESULTS,
                            handler = outfitFeature.handler,
                        )
                    },
                )
            }
        }
    }
}
