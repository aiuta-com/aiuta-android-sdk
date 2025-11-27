package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.buttons.ShareButton
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation.ImageListNavigationBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.collectAsLazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import kotlin.math.absoluteValue

@Composable
internal fun ImageListScreen(
    modifier: Modifier = Modifier,
    args: NavigationScreen.ImageListViewer,
) {
    val theme = LocalTheme.current
    val controller = LocalController.current

    val generatedImages = controller
        .generatedImageInteractor
        .generatedImagesFlow()
        .collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = args.pickedIndex,
        pageCount = { generatedImages.itemCount },
    )

    Box(
        modifier = modifier.background(color = theme.color.background),
    ) {
        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            key = { index -> generatedImages[index]?.id ?: index },
        ) { pageIndex ->
            AiutaImage(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = pagerState.offsetForPage(pageIndex)
                        translationY = pageOffset * size.height

                        alpha = when {
                            pageOffset > 0f -> 1f
                            else -> 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        }
                    }
                    .fillMaxSize(),
                shapeDp = 0.dp,
                imageUrl = generatedImages[pageIndex]?.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }

        ImageListNavigationBlock(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(top = 12.dp, start = 16.dp),
            pagerState = pagerState,
            generatedImages = generatedImages,
        )

        ShareElement {
            ShareButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 12.dp, end = 16.dp),
                isLoading = isShareActive,
                shareText = shareFeature.strings.shareButton,
                onClick = {
                    onShare(
                        activeProductItems = controller.activeProductItems,
                        imageUrl = generatedImages[pagerState.settledPage]?.imageUrl,
                        pageId = AiutaAnalyticsPageId.HISTORY,
                    )
                },
            )
        }
    }
}
