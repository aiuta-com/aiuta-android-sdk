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
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

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
            text = productItem.brand,
            style = theme.productBar.typography.brand,
            color = theme.color.primary,
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = productItem.title,
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
