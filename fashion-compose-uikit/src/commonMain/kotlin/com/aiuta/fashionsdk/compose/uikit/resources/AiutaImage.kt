package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.compose.uikit.internal.progress.ErrorProgress
import com.aiuta.fashionsdk.compose.uikit.internal.utils.placeholderFadeConnecting
import com.aiuta.fashionsdk.compose.uikit.resources.painter.painterResource

/**
 * Displays an image from a drawable resource.
 *
 * @param image The drawable resource to be displayed
 * @param contentDescription The content description for accessibility
 * @param modifier The modifier to be applied to the image
 * @param alignment The alignment of the image within its bounds
 * @param contentScale The scale type of the image
 * @param alpha The alpha value for the image
 * @param colorFilter Optional color filter to be applied to the image
 */
@Composable
public fun AiutaImage(
    image: AiutaDrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    // No need to show loading, because it's iconResource image
    Image(
        modifier = modifier,
        painter = painterResource(drawableResource = image),
        contentScale = contentScale,
        contentDescription = contentDescription,
        alignment = alignment,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

/**
 * Displays an image from a URL with rounded corners.
 *
 * @param imageUrl The URL of the image to be displayed
 * @param contentDescription The content description for accessibility
 * @param shapeDp The corner radius for the rounded corners
 * @param modifier The modifier to be applied to the image
 * @param imageBuilder The builder for configuring the image request
 * @param alignment The alignment of the image within its bounds
 * @param contentScale The scale type of the image
 * @param alpha The alpha value for the image
 * @param colorFilter Optional color filter to be applied to the image
 */
@Composable
public fun AiutaImage(
    imageUrl: String?,
    contentDescription: String?,
    shapeDp: Dp,
    modifier: Modifier = Modifier,
    imageBuilder: ImageRequest.Builder = ImageRequest.Builder(LocalPlatformContext.current),
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    AiutaImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        shape = RoundedCornerShape(shapeDp),
        modifier = modifier,
        imageBuilder = imageBuilder,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

/**
 * Displays an image from a URL with a custom shape.
 *
 * @param imageUrl The URL of the image to be displayed
 * @param contentDescription The content description for accessibility
 * @param shape The shape to be applied to the image
 * @param modifier The modifier to be applied to the image
 * @param imageBuilder The builder for configuring the image request
 * @param alignment The alignment of the image within its bounds
 * @param contentScale The scale type of the image
 * @param alpha The alpha value for the image
 * @param colorFilter Optional color filter to be applied to the image
 */
@Composable
public fun AiutaImage(
    imageUrl: String?,
    contentDescription: String?,
    shape: CornerBasedShape,
    modifier: Modifier = Modifier,
    imageBuilder: ImageRequest.Builder = ImageRequest.Builder(LocalPlatformContext.current),
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    val imagePainter = rememberAsyncImagePainter(
        model = imageBuilder
            .data(imageUrl)
            .crossfade(true)
            .build(),
    )
    val imageModelState = imagePainter.state.collectAsState()
    val isShimmerVisible = remember {
        derivedStateOf { imageModelState.value is AsyncImagePainter.State.Loading }
    }

    // Need box for more smooth transition
    Box(modifier = modifier) {
        when (imageModelState.value) {
            is AsyncImagePainter.State.Empty -> Unit
            is AsyncImagePainter.State.Error -> ErrorProgress(modifier = modifier)
            else -> {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .placeholderFadeConnecting(
                            shape = shape,
                            visible = isShimmerVisible.value,
                        ),
                    painter = imagePainter,
                    contentScale = contentScale,
                    alignment = alignment,
                    alpha = alpha,
                    colorFilter = colorFilter,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}
