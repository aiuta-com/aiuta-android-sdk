package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.ui.theme.AiutaTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer.blocks.common.alphaForBottomSheetConnection
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

internal fun LazyGridScope.activeProductsBlock(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    item(
        key = "ACTIVE_PRODUCTS_BLOCK",
        span = { GridItemSpan(maxLineSpan) },
        contentType = "ACTIVE_PRODUCTS_TYPE",
    ) {
        ActiveProductsBlock(
            modifier = modifier,
            generationResultController = generationResultController,
        )
    }
}

@Composable
private fun ActiveProductsBlock(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val activeProductItems = controller.activeProductItems
    val alphaRow = alphaForBottomSheetConnection(
        generationResultController = generationResultController,
    )

    LazyRow(
        modifier = modifier.alpha(alphaRow.value),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = activeProductItems,
            key = { item -> item.id },
        ) { item ->
            ActiveProductItem(
                productItem = item,
                controller = controller,
                theme = theme,
            )
        }
    }
}

@Composable
private fun ActiveProductItem(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    controller: FashionTryOnController,
    theme: AiutaTheme,
) {
    val productItemUrl = productItem.imageUrls.first()
    val commonImageModifier = Modifier
        .fillMaxWidth()
        .height(208.dp)
        .clip(theme.image.shapes.imageMShape)
    val imageModifier = if (theme.productBar.toggles.applyProductFirstImageExtraPadding) {
        commonImageModifier.padding(
            horizontal = 32.dp,
            vertical = 24.dp,
        )
    } else {
        commonImageModifier
    }

    Column(
        modifier = modifier
            .width(176.dp)
            .clickableUnindicated {
                controller.bottomSheetNavigator.show(
                    NavigationBottomSheetScreen.ProductInfo(
                        primaryButtonState = NavigationBottomSheetScreen.ProductInfo.PrimaryButtonState.ADD_TO_CART,
                        originPageId = AiutaAnalyticsPageId.RESULTS,
                        productItem = productItem,
                    ),
                )
            },
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = commonImageModifier.background(theme.color.neutral),
            contentAlignment = Alignment.Center,
        ) {
            AiutaImage(
                modifier = imageModifier,
                imageUrl = productItemUrl,
                shape = theme.image.shapes.imageMShape,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = productItem.brand,
            style = theme.productBar.typography.brand,
            color = theme.color.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = productItem.title,
            style = theme.productBar.typography.product,
            color = theme.color.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
