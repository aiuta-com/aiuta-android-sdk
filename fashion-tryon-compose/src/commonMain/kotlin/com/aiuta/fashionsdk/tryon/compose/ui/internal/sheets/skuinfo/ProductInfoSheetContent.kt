package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.block.ProductInfo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models.ProductInfoSheetEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.models.ProductInfoSheetViewState

private val IMAGE_SHAPE = 16.dp

@Composable
internal fun ProductInfoSheetContent(
    productInfo: TryOnBottomSheetScreen.ProductInfo,
    viewState: State<ProductInfoSheetViewState>,
    eventHandler: (ProductInfoSheetEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                        width = 170.dp,
                        height = 225.dp,
                    ),
                    imageUrl = imageUrl,
                    onClick = { imageSize, parentOffset, cornerRadius ->
                        eventHandler(
                            ProductInfoSheetEvent.ImageClicked(
                                imageUrl = imageUrl,
                                imageSize = imageSize,
                                parentOffset = parentOffset,
                                cornerRadius = cornerRadius,
                            ),
                        )
                    },
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sharedHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductInfo(
                modifier = Modifier.weight(1f),
                productItem = productInfo.productItem,
            )

            if (viewState.value.isWishlistAvailable) {
                Spacer(Modifier.width(12.dp))

                WishlistButton(
                    isInWishlist = viewState.value.isInWishlist,
                    onClick = { eventHandler(ProductInfoSheetEvent.WishlistToggled) },
                )
            }
        }

        Spacer(Modifier.height(34.dp))
    }
}

@Composable
private fun WishlistButton(
    isInWishlist: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wishlistFeature = provideFeature<AiutaWishlistFeature>() ?: return
    val theme = LocalTheme.current

    AiutaIcon(
        modifier = modifier
            .size(24.dp)
            .clickableUnindicated { onClick() },
        icon = if (isInWishlist) {
            wishlistFeature.icons.wishlistFill24
        } else {
            wishlistFeature.icons.wishlist24
        },
        contentDescription = null,
        tint = theme.color.primary,
    )
}

@Composable
private fun ImageContainer(
    imageUrl: String,
    onClick: (imageSize: Size, parentOffset: Offset, cornerRadius: Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    val sharedShape = RoundedCornerShape(IMAGE_SHAPE)

    AiutaImage(
        modifier = modifier
            .clip(sharedShape)
            .background(theme.color.background)
            .onGloballyPositioned { coordinates ->
                parentImageOffset = coordinates.positionInRoot()
                imageSize = coordinates.size.toSize()
            }
            .clickableUnindicated {
                onClick(imageSize, parentImageOffset, IMAGE_SHAPE)
            },
        imageUrl = imageUrl,
        shape = sharedShape,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}
