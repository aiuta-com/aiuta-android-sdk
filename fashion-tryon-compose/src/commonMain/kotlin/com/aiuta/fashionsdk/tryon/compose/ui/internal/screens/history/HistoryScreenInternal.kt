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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.SizeResolver.Companion.ORIGINAL
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaBoxedLoadingIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnLoadingActionsController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.deactivateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isSelectModeActive
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.analytic.sendDeleteHistoryEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.SelectorCard
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.common.HistoryAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.controller.HistoryScreenListeners
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.ShareInfo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils.calculateMinGridItemWidth
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils.deleteGeneratedImages

@Composable
internal fun HistoryScreen(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current

    sendPageEvent(pageId = AiutaAnalyticsPageId.HISTORY)

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        HistoryAppBar(
            modifier = Modifier
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
    val navigationController = LocalAiutaNavigationController.current
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
                val isLoading = remember {
                    derivedStateOf {
                        loadingActionsController.loadingGenerationsHolder.contain(
                            generatedImage,
                        )
                    }
                }
                val isSelectModeActive = controller.isSelectModeActive().value && !isLoading.value

                ImageContainer(
                    modifier = Modifier.animateItem(),
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
                                navigationController.navigateTo(
                                    newScreen = TryOnScreen.ImageListViewer(
                                        pickedIndex = index,
                                    ),
                                )
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
            .clip(theme.image.shapes.imageMShape)
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
            shape = theme.image.shapes.imageMShape,
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
                modifier = Modifier.fillMaxSize(),
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
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val loadingActionsController = LocalAiutaTryOnLoadingActionsController.current

    val generatedImages = getGeneratedImages()

    AnimatedVisibility(
        modifier =
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        visible = controller.isSelectModeActive().value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
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
            provideShareInfo = {
                ShareInfo(
                    activeProductItems = controller.activeProductItems,
                    imageUrls = controller
                        .selectorHolder
                        .getList()
                        .map { it.imageUrl },
                    pageId = AiutaAnalyticsPageId.HISTORY,
                )
            },
            onDelete = {
                controller.sendDeleteHistoryEvent()
                controller.deleteGeneratedImages(
                    errorSnackbarController = errorSnackbarController,
                    loadingActionsController = loadingActionsController,
                )
            },
        )
    }
}
