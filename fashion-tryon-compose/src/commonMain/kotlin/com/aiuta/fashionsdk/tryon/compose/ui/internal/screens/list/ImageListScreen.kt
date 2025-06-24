package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.collectAsLazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
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
            generatedImages[pageIndex]?.let { image ->
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = with(pagerState) {
                                (currentPage - pageIndex) + currentPageOffsetFraction
                            }
                            translationY = pageOffset * size.height
                            alpha = 1f - pageOffset.absoluteValue
                        }
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(image.imageUrl),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        }
    }
}
