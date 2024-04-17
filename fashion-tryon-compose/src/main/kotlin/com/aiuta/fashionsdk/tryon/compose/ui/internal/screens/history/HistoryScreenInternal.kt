package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.aiuta.fashionsdk.compose.tokens.FashionIcon
import com.aiuta.fashionsdk.compose.tokens.utils.clickableUnindicated
import com.aiuta.fashionsdk.internal.analytic.model.ShareGeneratedImage
import com.aiuta.fashionsdk.tryon.compose.R
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.ShareManager
import com.aiuta.fashionsdk.tryon.compose.domain.models.GeneratedImage
import com.aiuta.fashionsdk.tryon.compose.domain.models.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendShareGeneratedImageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.progress.LoadingProgress
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isSelectModeActive
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.analytic.sendOpenHistoryEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.SelectorCard
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils.deleteGeneratedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.openZoomImageScreen

private const val FULL_SIZE_SPAN = 3
private val SHARED_CORNER_RADIUS = 16.dp

@Composable
internal fun HistoryScreenInternal(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val theme = LocalTheme.current
    val generatedImages =
        controller
            .generatedImageInteractor
            .generatedImagesFlow()
            .collectAsLazyPagingItems()

    sendOpenHistoryEvent()

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = theme.colors.background),
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(FULL_SIZE_SPAN),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                span = { GridItemSpan(1) },
                count = generatedImages.itemCount,
                key = { generatedImages[it]?.id ?: 0 },
                contentType = { generatedImages.itemSnapshotList.getOrNull(it) },
            ) { index ->
                val generatedImage = generatedImages[index]
                val isSelectModeActive = controller.isSelectModeActive()

                var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
                var imageSize by remember { mutableStateOf(Size.Zero) }

                ImageContainer(
                    modifier =
                        Modifier
                            .onGloballyPositioned { coordinates ->
                                parentImageOffset = coordinates.positionInRoot()
                                imageSize = coordinates.size.toSize()
                            },
                    imageUrl = generatedImage?.imageUrl,
                    isEdit = isSelectModeActive.value,
                    isSelectedItem = controller.selectorHolder.contain(generatedImage),
                    onClick = {
                        when {
                            isSelectModeActive.value -> {
                                generatedImage?.let {
                                    controller.selectorHolder.putOrRemove(generatedImage)
                                }
                            }

                            else -> {
                                controller.zoomImageController.openZoomImageScreen(
                                    model =
                                        ZoomImageUiModel(
                                            imageSize = imageSize,
                                            initialCornerRadius = SHARED_CORNER_RADIUS,
                                            imageUrl = generatedImage?.imageUrl,
                                            parentImageOffset = parentImageOffset,
                                        ),
                                )
                            }
                        }
                    },
                )
            }
        }

        HistoryScreenEmpty(
            modifier = Modifier.fillMaxSize(),
            getGeneratedImages = { generatedImages },
        )

        HistoryScreenInterface(
            getGeneratedImages = { generatedImages },
        )
    }
}

@Composable
private fun ImageContainer(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    isEdit: Boolean = false,
    isSelectedItem: Boolean = false,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val theme = LocalTheme.current

    Box(
        modifier = modifier.clickableUnindicated { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .clip(RoundedCornerShape(SHARED_CORNER_RADIUS))
                    .background(color = theme.colors.background),
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

        AnimatedVisibility(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
            visible = isEdit,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(24.dp)
                        .border(width = 1.dp, color = theme.colors.onDark, shape = CircleShape)
                        .background(
                            color = if (isSelectedItem) theme.colors.aiuta else theme.colors.gray1,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                if (isSelectedItem) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(FashionIcon.Check16),
                        contentDescription = null,
                        tint = theme.colors.onDark,
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.HistoryScreenInterface(
    getGeneratedImages: () -> LazyPagingItems<GeneratedImage>,
) {
    val controller = LocalController.current
    val scope = rememberCoroutineScope()
    val generatedImages = getGeneratedImages()

    AnimatedVisibility(
        modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 8.dp),
        visible = controller.isSelectModeActive().value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        val context = LocalContext.current

        SelectorCard(
            modifier = Modifier.fillMaxWidth(),
            selectionMode = controller.selectorState,
            isActionActive = !controller.selectorHolder.isEmpty(),
            onSelectAll = {
                controller.selectorHolder.reset(generatedImages.itemSnapshotList.items)
                controller.selectorState.value = SelectorMode.ALL_IS_SELECTED
            },
            onDeselectAll = {
                controller.selectorHolder.removeAll()
                controller.selectorState.value = SelectorMode.ALL_IS_NOT_SELECTED
            },
            onStartSelectionMode = {
                controller.activateSelectMode()
            },
            onCancel = {
                controller.deactivateSelectMode()
            },
            onShare = {
                val shareManager = ShareManager(context)
                val imageUrls =
                    controller
                        .selectorHolder
                        .getList()
                        .map { it.imageUrl }

                controller.sendShareGeneratedImageEvent(
                    origin = ShareGeneratedImage.Origin.HISTORY_SCREEN,
                    count = imageUrls.size,
                )
                shareManager.share(imageUrls = imageUrls)

                controller.deactivateSelectMode()
            },
            onDelete = {
                controller.deleteGeneratedImages(scope)
            },
        )
    }
}

@Composable
private fun BoxScope.HistoryScreenEmpty(
    modifier: Modifier = Modifier,
    getGeneratedImages: () -> LazyPagingItems<GeneratedImage>,
) {
    val theme = LocalTheme.current
    val generatedImages = getGeneratedImages()

    if (generatedImages.itemCount == 0) {
        Column(
            modifier = modifier.background(theme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                imageVector = ImageVector.vectorResource(FashionIcon.Recent),
                contentDescription = null,
                tint = theme.colors.tertiary,
            )

            Spacer(Modifier.height(36.dp))

            Text(
                modifier = Modifier.padding(horizontal = 30.dp),
                text = stringResource(R.string.history_empty_description),
                style = MaterialTheme.typography.body1,
                color = theme.colors.primary,
                textAlign = TextAlign.Center,
            )
        }
    }
}
