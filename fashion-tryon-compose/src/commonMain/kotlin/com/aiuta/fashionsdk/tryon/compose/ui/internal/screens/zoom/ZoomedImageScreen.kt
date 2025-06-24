package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingComponent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.zoomable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.ShareElement
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share.onShare
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.ui.BaseSharedImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.utils.animateColorAsLerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.utils.animateDpAsLerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.utils.animateOffsetAsLerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.utils.animateSizeAsLerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.FitterContentScale
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.ZoomImageController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toDp
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ZoomedImageScreen(
    modifier: Modifier = Modifier,
    screenState: ZoomImageController,
) {
    BaseSharedImageScreen(
        controller = screenState,
    ) { sharedElementProgress ->
        val theme = LocalTheme.current

        val initialOffset = remember {
            screenState.transitionModel.value.parentImageOffset.copy(
                x = screenState.transitionModel.value.parentImageOffset.x,
            )
        }

        val contentScale = remember {
            FitterContentScale(sharedElementProgress)
        }

        val backgroundColor = animateColorAsLerp(
            start = Color.Transparent,
            stop = Color.Black,
            progress = sharedElementProgress,
        )

        val interfaceColor = animateColorAsLerp(
            start = Color.Transparent,
            stop = theme.color.onDark,
            progress = sharedElementProgress,
        )

        val imageOffset = animateOffsetAsLerp(
            start = initialOffset,
            stop = Offset.Zero,
            progress = sharedElementProgress,
        )

        val imageSize = animateSizeAsLerp(
            start = screenState.transitionModel.value.imageSize,
            stop = screenState.maxSize,
            progress = sharedElementProgress,
        )

        val cornerRadius = animateDpAsLerp(
            start = screenState.transitionModel.value.initialCornerRadius,
            stop = 0.dp,
            progress = sharedElementProgress,
        )

        ZoomedImageScreenContent(
            modifier = modifier,
            screenState = screenState,
            backgroundColor = backgroundColor,
            interfaceColor = interfaceColor,
            cornerRadius = cornerRadius,
            contentScale = contentScale,
            imageOffset = imageOffset,
            imageSize = imageSize,
        )
    }
}

@Composable
private fun ZoomedImageScreenContent(
    modifier: Modifier = Modifier,
    screenState: ZoomImageController,
    backgroundColor: State<Color>,
    interfaceColor: State<Color>,
    cornerRadius: State<Dp>,
    contentScale: ContentScale,
    imageOffset: State<IntOffset>,
    imageSize: State<Size>,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.background(color = backgroundColor.value),
    ) {
        AiutaImage(
            modifier = Modifier
                .offset { imageOffset.value }
                .size(
                    width = imageSize.value.width.toDp(LocalDensity.current),
                    height = imageSize.value.height.toDp(LocalDensity.current),
                )
                .clip(RoundedCornerShape(cornerRadius.value))
                .zoomable(
                    zoomState = screenState.imageZoomState,
                    onTap = {
                        screenState.closeZoomImageScreen(scope)
                    },
                ),
            imageUrl = screenState.transitionModel.value.imageUrl,
            shapeDp = cornerRadius.value,
            contentScale = contentScale,
            contentDescription = null,
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 14.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AiutaIcon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableUnindicated {
                        screenState.closeZoomImageScreen(scope)
                    },
                icon = theme.pageBar.icons.close24,
                contentDescription = null,
                tint = interfaceColor.value,
            )

            ShareElement {
                AiutaLoadingComponent(
                    isLoading = isShareActive.value,
                    circleSize = 24.dp,
                    circleColor = interfaceColor.value,
                ) {
                    Text(
                        modifier = Modifier
                            .clickableUnindicated(enabled = !isShareActive.value) {
                                onShare(
                                    activeProductItem = controller.activeProductItem.value,
                                    imageUrl = screenState.transitionModel.value.imageUrl,
                                    pageId = screenState.transitionModel.value.originPageId,
                                )
                            },
                        text = shareFeature.strings.shareButton,
                        style = theme.button.typography.buttonM,
                        color = interfaceColor.value,
                    )
                }
            }
        }
    }
}
