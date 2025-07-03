package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToCart
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block.ProductInfo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.changeActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationBottomSheetScreen.ProductInfo.PrimaryButtonState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.wishlist.inWishlistListener
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage

@Composable
internal fun ColumnScope.ProductInfoSheet(productInfo: NavigationBottomSheetScreen.ProductInfo) {
    val sharedHorizontalPadding = 16.dp

    SheetDivider()

    Spacer(Modifier.height(16.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = sharedHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(
            items = productInfo.productItem.imageUrls,
            key = { index, _ -> index },
        ) { _, imageUrl ->
            ImageContainer(
                modifier =
                Modifier.size(
                    width = 154.dp,
                    height = 202.dp,
                ),
                imageUrl = imageUrl,
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    ProductInfo(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = sharedHorizontalPadding),
        productItem = productInfo.productItem,
    )

    Spacer(Modifier.height(24.dp))

    ButtonsContainer(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = sharedHorizontalPadding),
        productInfo = productInfo,
    )
}

@Composable
private fun ImageContainer(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    val theme = LocalTheme.current

    val sharedShape = RoundedCornerShape(8.dp)

    AiutaImage(
        modifier = modifier
            .clip(sharedShape)
            .background(theme.color.background),
        imageUrl = imageUrl,
        shape = sharedShape,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Composable
private fun ButtonsContainer(
    modifier: Modifier = Modifier,
    productInfo: NavigationBottomSheetScreen.ProductInfo,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val activeSKUItem = controller.activeProductItem.value

    val tryOnFeature = strictProvideFeature<AiutaTryOnFeature>()
    val cartFeature = strictProvideFeature<AiutaTryOnCartFeature>()
    val wishlistFeature = provideFeature<AiutaWishlistFeature>()

    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        wishlistFeature?.let {
            val inWishlist = wishlistFeature.inWishlistListener()

            FashionButton(
                modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = if (inWishlist.value) {
                    wishlistFeature.icons.wishlistFill24
                } else {
                    wishlistFeature.icons.wishlist24
                },
                text = wishlistFeature.strings.wishlistButtonAdd,
                style = FashionButtonStyles.secondaryStyle(theme),
                size = FashionButtonSizes.lSize(iconSize = 20.dp),
                onClick = {
                    // TODO Migrate to list of product IDs with multi-try-on
                    controller.clickAddToWishListActiveSKU(
                        pageId = productInfo.originPageId,
                        updatedWishlistState = !inWishlist.value,
                        dataProvider = wishlistFeature.dataProvider,
                        productIds = listOf(activeSKUItem.id),
                    )
                },
            )

            Spacer(Modifier.width(8.dp))
        }

        FashionButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            text = if (productInfo.primaryButtonState == PrimaryButtonState.ADD_TO_CART) {
                cartFeature.strings.addToCart
            } else {
                tryOnFeature.strings.tryOn
            },
            icon = tryOnFeature.icons.tryOn20.takeIf {
                productInfo.primaryButtonState == PrimaryButtonState.TRY_ON
            },
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                if (productInfo.primaryButtonState == PrimaryButtonState.ADD_TO_CART) {
                    controller.clickAddToCart(
                        pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                        productId = productInfo.productItem.id,
                        handler = cartFeature.handler,
                    )
                } else {
                    controller.changeActiveSKU(productInfo.productItem)
                    controller.bottomSheetNavigator.hide()
                    controller.navigateBack()
                }
            },
        )
    }
}
