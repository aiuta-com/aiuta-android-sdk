package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common.IconLoadingButton
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common.LikeButton
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.wishlist.inWishlistListener

@Composable
internal fun ActionBlock(
    modifier: Modifier = Modifier,
    sessionImage: SessionImageUIModel,
) {
    val controller = LocalController.current

    val wishlistFeature = provideFeature<AiutaWishlistFeature>()

    val isShareActive = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShareElement {
            IconLoadingButton(
                icon = shareFeature.icons.share24,
                isLoading = isShareActive.value,
                onClick = {
                    onShare(
                        activeProductItems = controller.activeProductItems,
                        imageUrl = sessionImage.imageUrl,
                        pageId = AiutaAnalyticsPageId.RESULTS,
                    )
                },
            )
        }

        Spacer(Modifier.height(10.dp))
    }

    wishlistFeature?.let {
        val inWishlist = wishlistFeature.inWishlistListener()

        LikeButton(
            modifier = Modifier.size(38.dp),
            isLiked = inWishlist.value,
            iconSize = 24.dp,
            wishlistFeature = wishlistFeature,
            onClick = { currentState ->
                controller.clickAddToWishListActiveSKU(
                    pageId = AiutaAnalyticsPageId.RESULTS,
                    updatedWishlistState = !currentState,
                    dataProvider = wishlistFeature.dataProvider,
                    productIds = sessionImage.productIds,
                )
            },
        )
    }
}
