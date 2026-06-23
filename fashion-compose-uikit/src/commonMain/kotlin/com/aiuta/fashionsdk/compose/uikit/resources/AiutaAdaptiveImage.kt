package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiuta.fashionsdk.compose.uikit.internal.progress.ErrorProgress
import com.aiuta.fashionsdk.compose.uikit.internal.utils.placeholderFadeConnecting

/**
 * Photos whose aspect ratio (width / height) is below this threshold are considered
 * "too narrow to crop" (9:16 and narrower) and are rendered with [ContentScale.Fit] over a
 * blurred background. Everything wider (3:4 ≈ 0.75, 4:5, 1:1, …) is rendered with
 * [ContentScale.Crop] so it fills the container without side gaps.
 */
private const val ADAPTIVE_ASPECT_THRESHOLD = 0.72f

/** Decode size (px) of the blurred background copy — small decode + upscale gives the blur. */
private const val BLUR_BG_DECODE_PX = 48

/** Slight scale-up of the blurred background so its upscaled edges stay off-screen. */
private const val BLUR_BG_SCALE = 1.12f

/**
 * Real gaussian blur radius applied on top of the downsample, mirroring `blur(28px)`.
 * No-op on Android API < 31 (where the downsample alone provides the softening), real blur
 * on iOS / desktop / Android 31+.
 */
private val BLUR_BG_RADIUS = 28.dp

/** Color grading of the blurred background, mirroring `saturate(1.05) brightness(0.9)`. */
private const val BLUR_BG_SATURATION = 1.05f
private const val BLUR_BG_BRIGHTNESS = 0.9f

/**
 * Displays an image adaptively depending on its aspect ratio:
 * - aspectRatio >= [aspectThreshold] → [ContentScale.Crop], the image fills the container
 *   without side gaps (cover).
 * - aspectRatio < [aspectThreshold] → [ContentScale.Fit] (the whole subject stays visible),
 *   with the empty side areas filled by a blurred, slightly scaled, color-graded copy of the
 *   same image (contain + blurred background).
 *
 * The foreground is loaded at the view size via [rememberConstraintsSizeResolver] (instead of
 * `Size.ORIGINAL`), so large source images aren't decoded at full resolution. The cover / contain
 * decision is driven by a separate probe painter decoded at a fixed size, so the same image yields
 * the same aspect ratio (and thus the same mode) on every device, independent of container size.
 *
 * The blur is produced by decoding a tiny copy of the same image via Coil and upscaling it, then
 * (where supported) a real `Modifier.blur` on top — `blur` is a no-op below Android API 31, so
 * the downsample alone provides the softening there.
 *
 * @param model The Coil model to load — a URL [String] or raw image [ByteArray].
 * @param shapeDp The corner radius used to clip the container (and the shimmer placeholder).
 * @param contentDescription The content description for accessibility.
 * @param modifier The modifier applied to the root container. Callers should put sizing and
 *   any container-level modifiers (e.g. `hazeSource`, `onGloballyPositioned`, click) here.
 * @param alignment The alignment of the image within its bounds.
 * @param aspectThreshold The width/height boundary between cover and contain+blur.
 * @param onContentRectChanged Optional callback reporting the rect (in root coordinates) actually
 *   occupied by the painted image. In cover mode this equals the container; in contain mode it is
 *   the Fit-letterboxed sub-rect, so callers (e.g. shared-element zoom) animate from the real image
 *   bounds instead of the whole view.
 */
@Composable
public fun AiutaAdaptiveImage(
    model: Any?,
    contentDescription: String?,
    shapeDp: Dp? = null,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    aspectThreshold: Float = ADAPTIVE_ASPECT_THRESHOLD,
    onContentRectChanged: ((Rect) -> Unit)? = null,
) {
    val context = LocalPlatformContext.current

    val sizeResolver = rememberConstraintsSizeResolver()
    val foregroundPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(model)
            .size(sizeResolver)
            .crossfade(true)
            .build(),
    )
    val foregroundState by foregroundPainter.state.collectAsStateWithLifecycle()

    val ratioProbePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(model)
            .size(coil3.size.Size.ORIGINAL)
            .build(),
    )

    val isShimmerVisible by remember {
        derivedStateOf { foregroundState is AsyncImagePainter.State.Loading }
    }

    // null while the image is still loading (intrinsic size unknown) — render cover + shimmer.
    val aspectRatio by remember {
        derivedStateOf {
            val size = ratioProbePainter.intrinsicSize
            if (size.isSpecified && size.width > 0f && size.height > 0f) {
                size.width / size.height
            } else {
                null
            }
        }
    }

    val sharedShape = RoundedCornerShape(shapeDp ?: 0.dp)
    val ratio = aspectRatio
    val isContain = ratio != null && ratio < aspectThreshold

    // Track the container bounds so we can report the painted image rect (see onContentRectChanged).
    var containerOffset by remember { mutableStateOf(Offset.Unspecified) }
    var containerSize by remember { mutableStateOf(Size.Zero) }

    if (onContentRectChanged != null) {
        LaunchedEffect(containerOffset, containerSize, isContain, ratio) {
            if (containerOffset.isSpecified && containerSize.width > 0f && containerSize.height > 0f) {
                onContentRectChanged(
                    resolveContentRect(
                        containerOffset = containerOffset,
                        containerSize = containerSize,
                        isContain = isContain,
                        aspectRatio = ratio,
                    ),
                )
            }
        }
    }

    val positionModifier = if (onContentRectChanged != null) {
        Modifier.onGloballyPositioned { coordinates ->
            containerOffset = coordinates.positionInRoot()
            containerSize = coordinates.size.toSize()
        }
    } else {
        Modifier
    }

    Box(modifier = modifier.clip(sharedShape).then(positionModifier)) {
        if (isContain) {
            BlurredBackground(model = model)
        }

        // The size resolver must always be attached (even while loading) so Coil receives the
        // layout constraints and the request can resolve — otherwise it would never start.
        Image(
            modifier = Modifier
                .fillMaxSize()
                .then(sizeResolver)
                .placeholderFadeConnecting(
                    shape = sharedShape,
                    visible = isShimmerVisible,
                ),
            painter = foregroundPainter,
            contentScale = if (isContain) ContentScale.Fit else ContentScale.Crop,
            alignment = alignment,
            contentDescription = contentDescription,
        )

        if (foregroundState is AsyncImagePainter.State.Error) {
            ErrorProgress(modifier = Modifier.fillMaxSize())
        }
    }
}

/**
 * A blurred, slightly scaled, color-graded copy of [model], used to fill the side areas
 * behind a contained (Fit) image. The blur comes from decoding a tiny copy and upscaling it.
 */
@Composable
private fun BlurredBackground(model: Any?) {
    val context = LocalPlatformContext.current

    val colorFilter = remember {
        val matrix = ColorMatrix().apply { setToSaturation(BLUR_BG_SATURATION) }
        matrix.timesAssign(
            ColorMatrix(
                floatArrayOf(
                    BLUR_BG_BRIGHTNESS, 0f, 0f, 0f, 0f,
                    0f, BLUR_BG_BRIGHTNESS, 0f, 0f, 0f,
                    0f, 0f, BLUR_BG_BRIGHTNESS, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            ),
        )
        ColorFilter.colorMatrix(matrix)
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(model)
            .size(BLUR_BG_DECODE_PX)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = BLUR_BG_SCALE
                scaleY = BLUR_BG_SCALE
            }
            .blur(BLUR_BG_RADIUS),
        contentScale = ContentScale.Crop,
        colorFilter = colorFilter,
    )
}

/**
 * Resolves the rect actually covered by the painted image inside the container. For cover (or while
 * the aspect ratio is unknown) this is the full container; for contain it is the centered
 * Fit-letterboxed sub-rect.
 */
private fun resolveContentRect(
    containerOffset: Offset,
    containerSize: Size,
    isContain: Boolean,
    aspectRatio: Float?,
): Rect {
    if (!isContain || aspectRatio == null) {
        return Rect(containerOffset, containerSize)
    }

    val containerWidth = containerSize.width
    val containerHeight = containerSize.height

    // Fit: scale to fill width first, fall back to height when that would overflow.
    var width = containerWidth
    var height = containerWidth / aspectRatio
    if (height > containerHeight) {
        height = containerHeight
        width = containerHeight * aspectRatio
    }

    val left = containerOffset.x + (containerWidth - width) / 2f
    val top = containerOffset.y + (containerHeight - height) / 2f
    return Rect(Offset(left, top), Size(width, height))
}
