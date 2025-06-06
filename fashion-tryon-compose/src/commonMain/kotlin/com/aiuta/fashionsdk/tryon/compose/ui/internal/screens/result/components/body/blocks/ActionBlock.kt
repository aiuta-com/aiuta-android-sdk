package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.rememberShareManagerV2
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickAddToWishListActiveSKU
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common.IconLoadingButton
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common.LikeButton
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.wishlist.inWishlistListener
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.painter.painterResource
import kotlinx.coroutines.launch

@Composable
internal fun ActionBlock(
    modifier: Modifier = Modifier,
    imageUrl: String?,
) {
    val controller = LocalController.current

    val shareFeature = provideFeature<AiutaShareFeature>()
    val wishlistFeature = provideFeature<AiutaWishlistFeature>()

    val activeSKUItem = controller.activeProductItem.value
    val shareManager = rememberShareManagerV2()
    val scope = rememberCoroutineScope()
    val isShareActive = remember { mutableStateOf(false) }

    val watermarkPainter = shareFeature?.watermark?.images?.logo?.let { logo ->
        painterResource(logo)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        shareFeature?.let {
            IconLoadingButton(
                icon = shareFeature.icons.share24,
                isLoading = isShareActive.value,
                onClick = {
                    scope.launch {
                        isShareActive.value = true

                        val imageUrls = listOfNotNull(imageUrl)
                        val skuIds = listOf(controller.activeProductItem.value.id)
                        val shareText = shareFeature.dataProvider?.let { provider ->
                            provider::getShareText.safeInvoke(skuIds)
                        }

                        shareManager.shareImages(
                            content = shareText?.getOrNull(),
                            pageId = AiutaAnalyticsPageId.RESULTS,
                            productId = activeSKUItem.id,
                            imageUrls = imageUrls,
                            watermark = watermarkPainter,
                        )

                        isShareActive.value = false
                    }
                },
            )

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
                        productId = activeSKUItem.id,
                    )
                },
            )
        }
    }
}
