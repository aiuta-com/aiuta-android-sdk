package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState
import dev.chrisbanes.haze.HazeState

@Composable
internal fun ActionBlock(
    state: GenerationResultScreenViewState,
    sessionImage: SessionImageUIModel,
    hazeState: HazeState,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.isShareAvailable) {
            // Sharing keeps its own composable scope (ShareManager + watermark painter); analytics are
            // emitted inside ShareElement.onShare, so it does not route through the view model.
            ShareElement {
                ReactionIcon(
                    icon = shareFeature.icons.share24,
                    hazeState = hazeState,
                    isLoading = isShareActive.value,
                    onClick = {
                        onShare(
                            activeProductItems = state.products,
                            imageUrl = sessionImage.imageUrl,
                            pageId = AiutaAnalyticsPageId.RESULTS,
                        )
                    },
                )
            }
        }

        if (state.isWishlistAvailable) {
            provideFeature<AiutaWishlistFeature>()?.let { wishlistFeature ->
                ReactionIcon(
                    icon = if (state.isActiveProductInWishlist) {
                        wishlistFeature.icons.wishlistFill24
                    } else {
                        wishlistFeature.icons.wishlist24
                    },
                    hazeState = hazeState,
                    onClick = {
                        eventHandler(
                            GenerationResultScreenEvent.WishlistToggled(
                                sessionImage = sessionImage,
                                isAdding = !state.isActiveProductInWishlist,
                            ),
                        )
                    },
                )
            }
        }

        GenerateMoreBlock(
            state = state,
            sessionImage = sessionImage,
            hazeState = hazeState,
            eventHandler = eventHandler,
        )
    }
}
