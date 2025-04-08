package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.configuration.models.product.ProductItem
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun ProductInfo(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = productItem.store,
            style = theme.productBar.typography.brand,
            color = theme.color.primary,
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = productItem.description,
            style = theme.productBar.typography.product,
            color = theme.color.primary,
        )

        Spacer(Modifier.height(4.dp))

        PriceInfo(
            modifier = Modifier.fillMaxWidth(),
            productItem = productItem,
        )
    }
}
