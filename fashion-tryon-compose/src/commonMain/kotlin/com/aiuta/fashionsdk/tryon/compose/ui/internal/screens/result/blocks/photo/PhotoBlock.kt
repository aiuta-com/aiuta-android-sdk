package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaAdaptiveImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.components.navigation.GenerationIndicator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components.ActionBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components.DisclaimerStrip
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components.FeedbackBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.MAIN_IMAGE_SIZE
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.offsetForPage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlin.math.absoluteValue

/**
 * Height of [com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar] excluding the status-bar inset
 */
private val APP_BAR_CONTENT_HEIGHT = 52.dp

internal fun LazyListScope.photoBlock(
    viewState: State<GenerationResultScreenViewState>,
    pagerState: PagerState,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
) {
    item(
        key = "photo_block",
        contentType = "photo_block",
    ) {
        val screenSize = rememberScreenSize()
        val density = LocalDensity.current

        // Mirror the selector screen: the photo is MAIN_IMAGE_SIZE of the content area below the
        // app bar (screen minus status/navigation insets and the app bar), not of the whole screen.
        val statusBarHeight = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
        val navigationBarHeight = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
        val photoHeight = (screenSize.heightDp - statusBarHeight - navigationBarHeight - APP_BAR_CONTENT_HEIGHT) * MAIN_IMAGE_SIZE

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(photoHeight),
        ) {
            VerticalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                key = { index -> viewState.value.generations.getOrNull(index)?.id ?: index },
            ) { index ->
                val pageOffset = remember {
                    derivedStateOf { pagerState.offsetForPage(index).absoluteValue }
                }

                viewState.value.generations.getOrNull(index)?.let { sessionImage ->
                    PhotoCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val offset = pagerState.offsetForPage(index)
                                translationY = offset * size.height

                                alpha = when {
                                    offset > 0f -> 1f
                                    else -> 1f - offset.absoluteValue.coerceIn(0f, 1f)
                                }
                            },
                        sessionImage = sessionImage,
                        viewState = viewState,
                        pageOffset = pageOffset,
                        eventHandler = eventHandler,
                    )
                }
            }

            if (viewState.value.generations.size > 1) {
                GenerationIndicator(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    pagerState = pagerState,
                    generations = viewState.value.generations,
                )
            }
        }
    }
}

@Composable
private fun PhotoCard(
    sessionImage: SessionImageUIModel,
    viewState: State<GenerationResultScreenViewState>,
    pageOffset: State<Float>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val cornerRadius = 0.dp

    val hazeState = remember { HazeState() }
    var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = modifier.background(theme.color.background),
    ) {
        AiutaAdaptiveImage(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
                .clickableUnindicated {
                    eventHandler(
                        GenerationResultScreenEvent.PhotoClicked(
                            sessionImage = sessionImage,
                            imageSize = imageSize,
                            parentOffset = parentImageOffset,
                            cornerRadius = cornerRadius,
                        ),
                    )
                },
            model = sessionImage.imageUrl,
            shapeDp = cornerRadius,
            contentDescription = null,
            // Use the painted image rect (Fit-aware) so the zoom transition starts from the image,
            // not the full card — fixes the wrong origin for narrow (contain) photos.
            onContentRectChanged = { rect ->
                parentImageOffset = rect.topLeft
                imageSize = rect.size
            },
        )

        PhotoOverlay(
            modifier = Modifier.fillMaxSize(),
            sessionImage = sessionImage,
            viewState = viewState,
            hazeState = hazeState,
            pageOffset = pageOffset,
            eventHandler = eventHandler,
        )
    }
}

@Composable
private fun PhotoOverlay(
    sessionImage: SessionImageUIModel,
    viewState: State<GenerationResultScreenViewState>,
    hazeState: HazeState,
    pageOffset: State<Float>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewState.value
    val isInterfaceVisible = remember {
        derivedStateOf { pageOffset.value == 0f }
    }

    Column(modifier = modifier) {
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp, end = 12.dp),
            visible = isInterfaceVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ActionBlock(
                state = state,
                sessionImage = sessionImage,
                hazeState = hazeState,
                eventHandler = eventHandler,
            )
        }

        Spacer(Modifier.weight(1f))

        FeedbackBlock(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 12.dp, bottom = 14.dp),
            state = state,
            sessionImage = sessionImage,
            hazeState = hazeState,
            isInterfaceVisible = isInterfaceVisible,
            eventHandler = eventHandler,
        )

        state.fitDisclaimerText?.let { disclaimerText ->
            DisclaimerStrip(
                modifier = Modifier.fillMaxWidth(),
                text = disclaimerText,
                hazeState = hazeState,
                model = sessionImage.imageUrl,
                onClick = { eventHandler(GenerationResultScreenEvent.FitDisclaimerClicked) },
            )
        }
    }
}
