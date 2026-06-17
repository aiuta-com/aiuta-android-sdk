package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.products.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block.ProductInfo

/**
 * A single SKU row used in the result products block (Figma "Product_info"): a rounded thumbnail, the
 * brand/title/price column and a trailing chevron. The chevron reuses the page-bar back icon flipped
 * horizontally, tinted with the secondary color.
 */
@Composable
internal fun ProductRow(
    productItem: ProductItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    Row(
        modifier = modifier.clickableUnindicated { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AiutaImage(
            modifier = Modifier
                .size(width = 66.dp, height = 82.dp)
                .clip(theme.image.shapes.imageSShape)
                .border(
                    width = 1.dp,
                    color = theme.color.border,
                    shape = theme.image.shapes.imageSShape,
                ),
            imageUrl = productItem.imageUrls.firstOrNull(),
            shape = theme.image.shapes.imageSShape,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(Modifier.width(12.dp))

        ProductInfo(
            modifier = Modifier.weight(1f),
            productItem = productItem,
        )

        Spacer(Modifier.width(12.dp))

        AiutaIcon(
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer { scaleX = -1f },
            icon = theme.pageBar.icons.back24,
            contentDescription = null,
            tint = theme.color.secondary,
        )
    }
}
