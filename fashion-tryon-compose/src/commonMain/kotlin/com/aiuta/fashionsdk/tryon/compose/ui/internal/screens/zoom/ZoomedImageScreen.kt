package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.rememberShareManagerV2
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingComponent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.zoomable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.isTransitionActiveListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.FitterContentScale
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.ZoomImageController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.TRANSITION_ANIM_DURATION
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toDp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toIntOffset
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.painter.painterResource
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated
import kotlinx.coroutines.launch

@Composable
internal fun ZoomedImageScreen(
    modifier: Modifier = Modifier,
    screenState: ZoomImageController,
    onTransitionFinished: () -> Unit,
) {
    val theme = LocalTheme.current
    val isTransitionActive = screenState.isTransitionActiveListener()

    val initialOffset = remember {
        screenState.transitionModel.value.parentImageOffset.copy(
            x = screenState.transitionModel.value.parentImageOffset.x,
        )
    }

    val sharedElementProgress = remember { Animatable(if (isTransitionActive.value) 0f else 1f) }

    val contentScale = remember {
        FitterContentScale(sharedElementProgress)
    }

    val backgroundColor =
        remember {
            derivedStateOf {
                lerp(
                    Color.Transparent,
                    Color.Black,
                    sharedElementProgress.value,
                )
            }
        }

    val interfaceColor =
        remember {
            derivedStateOf {
                lerp(
                    Color.Transparent,
                    theme.color.onDark,
                    sharedElementProgress.value,
                )
            }
        }

    val imageOffset =
        remember {
            derivedStateOf {
                lerp(
                    initialOffset,
                    Offset.Zero,
                    sharedElementProgress.value,
                ).toIntOffset()
            }
        }

    val imageSize =
        remember {
            derivedStateOf {
                lerp(
                    screenState.transitionModel.value.imageSize,
                    screenState.maxSize,
                    sharedElementProgress.value,
                )
            }
        }

    val cornerRadius =
        remember {
            derivedStateOf {
                lerp(
                    screenState.transitionModel.value.initialCornerRadius,
                    0.dp,
                    sharedElementProgress.value,
                )
            }
        }

    LaunchedEffect(isTransitionActive) {
        launch {
            sharedElementProgress.animateTo(
                targetValue = if (isTransitionActive.value) 1f else 0f,
                animationSpec = tween(durationMillis = TRANSITION_ANIM_DURATION),
            )
            onTransitionFinished()
        }
    }

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

    val shareFeature = provideFeature<AiutaShareFeature>()

    val scope = rememberCoroutineScope()
    val shareManager = rememberShareManagerV2()
    val isShareActive = remember { mutableStateOf(false) }

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

            shareFeature?.let {
                val watermarkPainter = shareFeature
                    .watermark
                    ?.images
                    ?.logo
                    ?.let { logo -> painterResource(logo) }

                AiutaLoadingComponent(
                    isLoading = isShareActive.value,
                    circleSize = 24.dp,
                    circleColor = interfaceColor.value,
                ) {
                    Text(
                        modifier = Modifier
                            .clickableUnindicated(enabled = !isShareActive.value) {
                                scope.launch {
                                    isShareActive.value = true

                                    val imageUrls = listOfNotNull(screenState.transitionModel.value.imageUrl)
                                    val skuIds = listOf(controller.activeProductItem.value.id)
                                    val shareText = shareFeature.dataProvider?.let { provider ->
                                        provider::getShareText.safeInvoke(skuIds)
                                    }

                                    shareManager.shareImages(
                                        content = shareText?.getOrNull(),
                                        pageId = screenState.transitionModel.value.originPageId,
                                        productId = controller.activeProductItem.value.id,
                                        imageUrls = imageUrls,
                                        watermark = watermarkPainter,
                                    )

                                    isShareActive.value = false
                                }
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
