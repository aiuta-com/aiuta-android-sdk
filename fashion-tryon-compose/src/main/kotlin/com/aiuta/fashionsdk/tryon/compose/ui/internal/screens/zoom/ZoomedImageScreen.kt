package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.rememberAsyncImagePainter
import com.aiuta.fashionsdk.compose.tokens.FashionIcon
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.compose.tokens.utils.clickableUnindicated
import com.aiuta.fashionsdk.internal.analytic.model.ShareGeneratedImage
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.ShareManager
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendShareGeneratedImageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.zoomable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnStringResources
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.FitterContentScale
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.ZoomImageController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.closeZoomImageScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.isTransitionActiveListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.TRANSITION_ANIM_DURATION
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toDp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toIntOffset
import kotlinx.coroutines.launch

@Composable
internal fun ZoomedImageScreen(
    modifier: Modifier = Modifier,
    screenState: ZoomImageController,
    onTransitionFinished: () -> Unit,
) {
    val theme = LocalTheme.current
    val isTransitionActive = screenState.isTransitionActiveListener()

    val initialOffset =
        remember {
            screenState.sharedImage.value.parentImageOffset.copy(
                x = screenState.sharedImage.value.parentImageOffset.x,
            )
        }

    val sharedElementProgress = remember { Animatable(if (isTransitionActive.value) 0f else 1f) }

    val contentScale =
        remember {
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
                    theme.colors.onDark,
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
                    screenState.sharedImage.value.imageSize,
                    screenState.maxSize,
                    sharedElementProgress.value,
                )
            }
        }

    val cornerRadius =
        remember {
            derivedStateOf {
                lerp(
                    screenState.sharedImage.value.initialCornerRadius,
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
    val context = LocalContext.current
    val controller = LocalController.current
    val theme = LocalTheme.current
    val stringResources = LocalAiutaTryOnStringResources.current

    val scope = rememberCoroutineScope()
    val shareManager =
        remember {
            ShareManager(context)
        }

    Box(
        modifier = modifier.background(color = backgroundColor.value),
    ) {
        Image(
            modifier =
                Modifier
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
            painter =
                rememberAsyncImagePainter(
                    model = screenState.sharedImage.value.imageUrl,
                ),
            contentScale = contentScale,
            contentDescription = null,
        )

        Icon(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(12.dp)
                    .clickableUnindicated {
                        screenState.closeZoomImageScreen(scope)
                    },
            imageVector = ImageVector.vectorResource(FashionIcon.Cross36),
            contentDescription = null,
            tint = interfaceColor.value,
        )

        Text(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(16.dp)
                    .clickableUnindicated {
                        val imageUrls = listOfNotNull(screenState.sharedImage.value.imageUrl)
                        controller.sendShareGeneratedImageEvent(
                            origin = ShareGeneratedImage.Origin.RESULT_FULLSCREEN,
                            count = imageUrls.size,
                            additionalShareInfo = screenState.sharedImage.value.additionalShareInfo,
                        )
                        shareManager.share(
                            content = screenState.sharedImage.value.additionalShareInfo,
                            imageUrls = imageUrls,
                            watermarkRes = theme.watermarkRes,
                            origin = ShareGeneratedImage.Origin.RESULT_FULLSCREEN,
                        )
                    },
            text = stringResources.share,
            style = MaterialTheme.typography.h6,
            color = interfaceColor.value,
        )
    }
}
