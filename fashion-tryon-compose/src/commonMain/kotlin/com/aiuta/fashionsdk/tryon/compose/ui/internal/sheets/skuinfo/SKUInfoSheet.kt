package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.cart.AiutaTryOnCartFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddProductToCart
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block.ProductInfo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen.ProductInfo.PrimaryButtonState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.openScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.wishlist.inWishlistListener

@Composable
internal fun ProductInfoSheet(
    productInfo: TryOnBottomSheetScreen.ProductInfo,
    modifier: Modifier = Modifier,
) {
    val controller = LocalController.current
    val sharedHorizontalPadding = 16.dp

    Column(modifier = modifier) {
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
                    modifier = Modifier.size(
                        width = 154.dp,
                        height = 202.dp,
                    ),
                    imageUrl = imageUrl,
                    controller = controller,
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
}

@Composable
private fun ImageContainer(
    modifier: Modifier = Modifier,
    imageUrl: String,
    controller: FashionTryOnController,
) {
    val theme = LocalTheme.current

    var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    val sharedShapeDp = 8.dp
    val sharedShape = RoundedCornerShape(sharedShapeDp)

    AiutaImage(
        modifier = modifier
            .clip(sharedShape)
            .background(theme.color.background)
            .onGloballyPositioned { coordinates ->
                parentImageOffset = coordinates.positionInRoot()
                imageSize = coordinates.size.toSize()
            }
            .clickableUnindicated {
                controller.zoomImageController.openScreen(
                    model = ZoomImageUiModel(
                        imageSize = imageSize,
                        initialCornerRadius = sharedShapeDp,
                        imageUrl = imageUrl,
                        parentImageOffset = parentImageOffset,
                        originPageId = AiutaAnalyticsPageId.RESULTS,
                    ),
                )
            },
        imageUrl = imageUrl,
        shape = sharedShape,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Composable
private fun ButtonsContainer(
    modifier: Modifier = Modifier,
    productInfo: TryOnBottomSheetScreen.ProductInfo,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

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
                    controller.clickAddToWishListActiveSKU(
                        pageId = productInfo.originPageId,
                        updatedWishlistState = !inWishlist.value,
                        dataProvider = wishlistFeature.dataProvider,
                    )
                },
            )

            Spacer(Modifier.width(8.dp))
        }

        FashionButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            text = when (productInfo.primaryButtonState) {
                PrimaryButtonState.ADD_TO_CART -> cartFeature.strings.addToCart
            },
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                when (productInfo.primaryButtonState) {
                    PrimaryButtonState.ADD_TO_CART -> {
                        controller.clickAddProductToCart(
                            pageId = AiutaAnalyticsPageId.IMAGE_PICKER,
                            productId = productInfo.productItem.id,
                            handler = cartFeature.handler,
                        )
                    }
                }
            },
        )
    }
}
