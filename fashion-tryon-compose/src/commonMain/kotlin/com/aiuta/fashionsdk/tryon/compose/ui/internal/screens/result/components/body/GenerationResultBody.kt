package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.SizeResolver.Companion.ORIGINAL
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.openScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks.ActionBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks.FeedbackBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks.GenerateMoreBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.MAIN_IMAGE_SIZE
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.calculateCurrentOffsetForPage
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlin.math.absoluteValue

@Composable
internal fun GenerationResultBody(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val controller = LocalController.current
    val pagerState = generationResultController.generationPagerState

    val generations = controller.sessionGenerationInteractor.sessionGenerations

    val horizontalPaddingWeight = 1 - MAIN_IMAGE_SIZE
    val screenSize = rememberScreenSize()
    val contentHorizontalPadding = (screenSize.widthDp * horizontalPaddingWeight) / 2

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = contentHorizontalPadding),
        pageSpacing = 16.dp,
    ) { index ->
        val pageOffset =
            remember {
                derivedStateOf {
                    pagerState.calculateCurrentOffsetForPage(index).absoluteValue
                }
            }

        val alphaItem =
            remember {
                derivedStateOf {
                    (1 - pageOffset.value).coerceAtLeast(0.3f)
                }
            }

        val heightFraction =
            remember {
                derivedStateOf {
                    (1 - pageOffset.value).coerceAtLeast(0.9f)
                }
            }

        generations.getOrNull(index)?.let { sessionImage ->
            PagerItem(
                modifier =
                Modifier
                    .graphicsLayer {
                        alpha = alphaItem.value
                    }
                    .fillMaxWidth()
                    .fillMaxHeight(heightFraction.value),
                sessionImage = sessionImage,
                generationResultController = generationResultController,
                pageOffset = pageOffset,
            )
        }
    }
}

@Composable
private fun PagerItem(
    modifier: Modifier = Modifier,
    sessionImage: SessionImageUIModel,
    generationResultController: GenerationResultController,
    pageOffset: State<Float>,
) {
    val controller = LocalController.current
    val coilContext = LocalPlatformContext.current
    val theme = LocalTheme.current

    val sharedCornerRadius = theme.image.shapes.imageL

    val hazeState = remember { HazeState() }
    var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier
            .clip(RoundedCornerShape(sharedCornerRadius))
            .background(
                color = theme.color.background,
                shape = RoundedCornerShape(sharedCornerRadius),
            ),
    ) {
        AiutaImage(
            modifier = Modifier
                .clipToBounds()
                .fillMaxSize()
                .hazeSource(hazeState)
                .onGloballyPositioned { coordinates ->
                    parentImageOffset = coordinates.positionInRoot()
                    imageSize = coordinates.size.toSize()
                }
                .clickableUnindicated {
                    controller.zoomImageController.openScreen(
                        model = ZoomImageUiModel(
                            imageSize = imageSize,
                            initialCornerRadius = sharedCornerRadius,
                            imageUrl = sessionImage.imageUrl,
                            parentImageOffset = parentImageOffset,
                            originPageId = AiutaAnalyticsPageId.RESULTS,
                        ),
                    )
                },
            imageUrl = sessionImage.imageUrl,
            shapeDp = sharedCornerRadius,
            imageBuilder = ImageRequest.Builder(coilContext).size(ORIGINAL),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        PagerItemInterface(
            modifier = Modifier.fillMaxSize(),
            sessionImage = sessionImage,
            generationResultController = generationResultController,
            hazeState = hazeState,
            pageOffset = pageOffset,
        )
    }
}

@Composable
internal fun BoxScope.PagerItemInterface(
    modifier: Modifier = Modifier,
    sessionImage: SessionImageUIModel,
    generationResultController: GenerationResultController,
    hazeState: HazeState,
    pageOffset: State<Float>,
) {
    val isVisible =
        remember {
            derivedStateOf {
                pageOffset.value == 0f
            }
        }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ActionBlock(
                modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                imageUrl = sessionImage.imageUrl,
            )

            GenerateMoreBlock(
                modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
            )
        }
    }

    FeedbackBlock(
        modifier =
        Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp),
        sessionImage = sessionImage,
        hazeState = hazeState,
        generationResultController = generationResultController,
        isInterfaceVisible = isVisible,
    )
}
