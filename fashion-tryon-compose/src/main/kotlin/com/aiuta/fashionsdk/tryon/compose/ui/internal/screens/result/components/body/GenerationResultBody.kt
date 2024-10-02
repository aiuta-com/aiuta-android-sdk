package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.compose.tokens.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.progress.LoadingProgress
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.subscribeToSuccessOperations
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.openZoomImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.MAIN_IMAGE_SIZE

@Composable
internal fun BoxWithConstraintsScope.GenerationResultBody(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val controller = LocalController.current

    val successOperations = controller.subscribeToSuccessOperations()
    val generationUrls =
        remember(successOperations.value) {
            successOperations.value.flatMap { it.generatedImageUrls }
        }

    val horizontalPaddingWeight = 1 - MAIN_IMAGE_SIZE
    val contentHorizontalPadding = (maxWidth * horizontalPaddingWeight) / 2

    HorizontalPager(
        modifier = modifier,
        state = generationResultController.generationPagerState,
        contentPadding = PaddingValues(horizontal = contentHorizontalPadding),
        pageSpacing = 16.dp,
    ) { index ->
        PagerItem(
            modifier = Modifier.fillMaxSize(),
            imageUrl = generationUrls.getOrNull(index),
            itemIndex = index,
            generationResultController = generationResultController,
        )
    }
}

@Composable
private fun PagerItem(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    itemIndex: Int,
    generationResultController: GenerationResultController,
) {
    val controller = LocalController.current
    val context = LocalContext.current
    val theme = LocalTheme.current
    val sharedCornerRadius = 24.dp

    var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier
            .clip(RoundedCornerShape(sharedCornerRadius))
            .background(
                color = theme.colors.background,
                shape = RoundedCornerShape(sharedCornerRadius),
            ),
    ) {
        SubcomposeAsyncImage(
            modifier =
                Modifier
                    .clipToBounds()
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        parentImageOffset = coordinates.positionInRoot()
                        imageSize = coordinates.size.toSize()
                    }
                    .clickableUnindicated {
                        controller.zoomImageController.openZoomImageScreen(
                            model =
                                ZoomImageUiModel(
                                    imageSize = imageSize,
                                    initialCornerRadius = sharedCornerRadius,
                                    imageUrl = imageUrl,
                                    parentImageOffset = parentImageOffset,
                                    additionalShareInfo = controller.activeSKUItem.value.additionalShareInfo,
                                ),
                        )
                    },
            model =
                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
            loading = {
                LoadingProgress(modifier = Modifier.fillMaxSize())
            },
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        // TODO
//        ShareButton(
//            modifier =
//            Modifier
//                .align(Alignment.TopEnd)
//                .padding(10.dp)
//                .size(38.dp)
//                .shadow(
//                    elevation = 4.dp,
//                    spotColor = Color.Black.copy(0.2f),
//                    ambientColor = Color.Black.copy(0.2f),
//                ),
//            imageUrl = imageUrl,
//        )

        FeedbackBlock(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(14.dp),
            itemIndex = itemIndex,
            generationResultController = generationResultController,
        )
    }
}
