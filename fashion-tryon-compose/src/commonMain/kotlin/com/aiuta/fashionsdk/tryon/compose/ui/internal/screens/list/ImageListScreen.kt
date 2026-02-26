package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingComponent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation.GenerationIndicator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import kotlin.math.absoluteValue

@Composable
internal fun ImageListScreen(
    modifier: Modifier = Modifier,
    args: TryOnScreen.ImageListViewer,
) {
    val theme = LocalTheme.current
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current

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

        GenerationIndicator(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 16.dp),
            pagerState = pagerState,
            generatedImages = generatedImages,
        )

        AiutaAppBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            navigationIcon = {
                AiutaAppBarIcon(
                    modifier = Modifier.align(Alignment.CenterStart),
                    icon = theme.pageBar.icons.close24,
                    color = theme.color.onDark,
                    onClick = navigationController::navigateBack,
                )
            },
            actions = {
                ShareElement {
                    AiutaLoadingComponent(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        circleColor = theme.color.onLight,
                        circleSize = 24.dp,
                        isLoading = isShareActive.value,
                        component = {
                            Text(
                                modifier = Modifier.clickableUnindicated {
                                    onShare(
                                        activeProductItems = controller.activeProductItems,
                                        imageUrl = generatedImages[pagerState.settledPage]?.imageUrl,
                                        pageId = AiutaAnalyticsPageId.HISTORY,
                                    )
                                },
                                style = theme.button.typography.buttonM,
                                color = theme.color.onDark,
                                textAlign = TextAlign.Center,
                                text = shareFeature.strings.shareButton,
                            )
                        },
                    )
                }
            },
        )
    }
}
