@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.SizeResolver.Companion.ORIGINAL
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsHistoryEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaBoxedLoadingIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isSelectModeActive
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.analytic.sendHistoryEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.SelectorCard
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.common.HistoryAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.controller.HistoryScreenListeners
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils.calculateMinGridItemWidth
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils.deleteGeneratedImages
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.LazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.collectAsLazyPagingItems
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.itemContentType
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.paging.itemKey
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun HistoryScreen(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current

    sendPageEvent(pageId = AiutaAnalyticsPageId.HISTORY)

    Column(
        modifier = modifier.background(theme.color.background),
    ) {
        HistoryAppBar(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        HistoryScreenInternal(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun HistoryScreenInternal(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val loadingActionsController = LocalAiutaTryOnLoadingActionsController.current
    val theme = LocalTheme.current

    val minColumnsCount = 3
    val contentPadding = 8.dp
    val horizontalPadding = 8.dp

    val columnMinWidth = calculateMinGridItemWidth(
        preferredWidth = 120.dp,
        minColumnsCount = minColumnsCount,
        contentPadding = contentPadding,
        horizontalPadding = horizontalPadding,
    )

    val generatedImages = controller
        .generatedImageInteractor
        .generatedImagesFlow()
        .collectAsLazyPagingItems()

    val sharedRadius = theme.image.shapes.imageS

    HistoryScreenListeners(generatedImages = generatedImages)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = theme.color.background),
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = columnMinWidth),
            contentPadding = PaddingValues(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(horizontalPadding),
        ) {
            items(
                span = { GridItemSpan(1) },
                count = generatedImages.itemCount,
                key = generatedImages.itemKey { it.id },
                contentType = generatedImages.itemContentType { "HISTORY_CONTENT_TYPE" },
            ) { index ->
                val generatedImage = generatedImages[index]

                var parentImageOffset by remember { mutableStateOf(Offset.Unspecified) }
                var imageSize by remember { mutableStateOf(Size.Zero) }

                val isLoading = remember {
                    derivedStateOf {
                        loadingActionsController.loadingGenerationsHolder.contain(
                            generatedImage,
                        )
                    }
                }
                val isSelectModeActive = controller.isSelectModeActive().value && !isLoading.value

                ImageContainer(
                    modifier = Modifier
                        .animateItem()
                        .onGloballyPositioned { coordinates ->
                            parentImageOffset = coordinates.positionInRoot()
                            imageSize = coordinates.size.toSize()
                        },
                    imageUrl = generatedImage?.imageUrl,
                    isEdit = isSelectModeActive,
                    isSelectedItem = controller.selectorHolder.contain(generatedImage),
                    isLoading = isLoading.value,
                    onClick = {
                        when {
                            // Don't click if loading
                            isLoading.value -> Unit

                            isSelectModeActive -> {
                                generatedImage?.let {
                                    controller.selectorHolder.putOrRemove(generatedImage)
                                }
                            }

                            else -> {
                                controller.navigateTo(
                                    newScreen = NavigationScreen.ImageListViewer(
                                        pickedIndex = index,
                                    ),
                                )
//                                controller.zoomImageController.openScreen(
//                                    model = ZoomImageUiModel(
//                                        imageSize = imageSize,
//                                        initialCornerRadius = sharedRadius,
//                                        imageUrl = generatedImage?.imageUrl,
//                                        parentImageOffset = parentImageOffset,
//                                        originPageId = AiutaAnalyticsPageId.HISTORY,
//                                    ),
//                                )
                            }
                        }
                    },
                )
            }
        }

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
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val coilContext = LocalPlatformContext.current
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .clip(theme.image.shapes.imageSShape)
            .background(color = theme.color.background)
            .clickableUnindicated { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        AiutaImage(
            modifier = Modifier
                .clipToBounds()
                .fillMaxSize(),
            // Do that, because thumbnail size is too small for zoom screen
            imageBuilder = ImageRequest.Builder(coilContext).size(ORIGINAL),
            imageUrl = imageUrl,
            shape = theme.image.shapes.imageSShape,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        AnimatedVisibility(
            modifier = Modifier
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
                    .border(width = 1.dp, color = theme.color.onDark, shape = CircleShape)
                    .background(
                        color = if (isSelectedItem) theme.color.brand else theme.color.neutral,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isSelectedItem) {
                    AiutaIcon(
                        modifier = Modifier.size(20.dp),
                        icon = theme.selectionSnackbar.icons.check20,
                        contentDescription = null,
                        tint = theme.color.onDark,
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .clipToBounds()
                .fillMaxSize(),
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AiutaBoxedLoadingIcon(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                circleColor = Color.White,
            )
        }
    }
}

@Composable
private fun BoxScope.HistoryScreenInterface(
    getGeneratedImages: () -> LazyPagingItems<GeneratedImageUIModel>,
) {
    val controller = LocalController.current
    val loadingActionsController = LocalAiutaTryOnLoadingActionsController.current

    val generatedImages = getGeneratedImages()

    AnimatedVisibility(
        modifier =
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        visible = controller.isSelectModeActive().value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ShareElement {
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
                onCancel = {
                    controller.deactivateSelectMode()
                },
                isShareLoading = isShareActive.value,
                onShare = {
                    onShare(
                        activeProductItems = listOf(controller.activeProductItem.value),
                        imageUrls = controller
                            .selectorHolder
                            .getList()
                            .map { it.imageUrl },
                        pageId = AiutaAnalyticsPageId.HISTORY,
                    )
                },
                onDelete = {
                    controller.sendHistoryEvent(AiutaAnalyticsHistoryEventType.GENERATED_IMAGE_DELETED)
                    controller.deleteGeneratedImages(
                        loadingActionsController = loadingActionsController,
                    )
                },
            )
        }
    }
}
