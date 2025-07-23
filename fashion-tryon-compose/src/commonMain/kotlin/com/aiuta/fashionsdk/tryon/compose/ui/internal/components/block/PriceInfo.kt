package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun PriceInfo(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
) {
    val theme = LocalTheme.current
    val priceTheme = theme.productBar.prices

    if (priceTheme == null) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        productItem.price?.let { price ->
            price.old?.let { oldPrice ->
                Text(
                    text = oldPrice,
                    style = priceTheme.typography.price,
                    color = priceTheme.colors.discountedPrice,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.width(4.dp))
            }

            Text(
                text = price.current,
                style = priceTheme.typography.price.copy(
                    textDecoration = solveGeneralPriceDecoration(productItem),
                ),
                color = solveGeneralPriceColor(productItem),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
