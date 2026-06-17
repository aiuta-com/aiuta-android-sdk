package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils.toImageBitmap
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

/** Alpha of the white frost the strip lays over the photo — also used to estimate effective brightness. */
private const val STRIP_FROST_ALPHA = 0.4f

/** Decode size (px) of the tiny copy used only to estimate the brightness behind the strip. */
private const val BRIGHTNESS_SAMPLE_PX = 32

/** Only the bottom slice of the photo sits behind the strip, so sample from here down. */
private const val BRIGHTNESS_SAMPLE_BOTTOM_FRACTION = 0.8f

/** Effective (frosted) luminance above which the photo reads as "light" and needs dark text. */
private const val LIGHT_LUMINANCE_THRESHOLD = 0.7f

/**
 * Blurred fit-disclaimer strip pinned to the bottom of the result photo card. Tapping it opens the
 * fit-disclaimer sheet (handled by the caller via [onClick]).
 *
 * The text/icon color adapts to the photo: over a bright bottom area it switches to the primary
 * (dark) color, otherwise it stays on the on-dark (light) color.
 */
@Composable
internal fun DisclaimerStrip(
    text: String,
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    model: Any? = null,
) {
    val theme = LocalTheme.current
    val fitDisclaimerFeature = provideFeature<AiutaTryOnFitDisclaimerFeature>()

    val contentColor = if (rememberIsBackgroundLight(model)) {
        theme.color.primary
    } else {
        theme.color.onDark
    }

    Row(
        modifier = modifier
            .hazeEffect(hazeState) {
                val sharedColor = theme.color.background.copy(alpha = STRIP_FROST_ALPHA)

                blurRadius = 10.dp
                backgroundColor = sharedColor
                tints = listOf(HazeTint(sharedColor))
                fallbackTint = HazeTint(sharedColor)
            }
            .clickableUnindicated { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            style = theme.label.typography.footnote,
            color = contentColor,
            textAlign = TextAlign.Center,
        )

        fitDisclaimerFeature?.icons?.info20?.let { info20 ->
            Spacer(Modifier.width(4.dp))

            AiutaIcon(
                modifier = Modifier.size(20.dp),
                icon = info20,
                contentDescription = null,
                tint = contentColor,
            )
        }
    }
}

/**
 * Decodes a tiny copy of [model] and estimates the brightness of the photo area behind the strip.
 * Returns true when the frosted result is light enough that dark text reads better. Falls back to
 * false (light text) while loading or on any failure.
 */
@Composable
private fun rememberIsBackgroundLight(model: Any?): Boolean {
    if (model == null) return false

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalPlatformContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(model)
            .size(BRIGHTNESS_SAMPLE_PX)
            .build(),
    )
    val state by painter.state.collectAsStateWithLifecycle()

    return remember(state) {
        if (state !is AsyncImagePainter.State.Success) return@remember false

        runCatching {
            val bottomLuminance = painter
                .toImageBitmap(density, layoutDirection)
                .bottomRegionLuminance(BRIGHTNESS_SAMPLE_BOTTOM_FRACTION)

            // Blend the sampled photo brightness with the white frost the strip adds on top.
            val frostedLuminance = STRIP_FROST_ALPHA + (1f - STRIP_FROST_ALPHA) * bottomLuminance
            frostedLuminance > LIGHT_LUMINANCE_THRESHOLD
        }.getOrDefault(false)
    }
}

private fun ImageBitmap.bottomRegionLuminance(bottomFraction: Float): Float {
    val pixelMap = toPixelMap()
    if (pixelMap.width == 0 || pixelMap.height == 0) return 0f

    val startY = (pixelMap.height * bottomFraction).toInt().coerceIn(0, pixelMap.height - 1)
    var sum = 0f
    var count = 0
    for (y in startY until pixelMap.height) {
        for (x in 0 until pixelMap.width) {
            sum += pixelMap[x, y].luminance()
            count++
        }
    }
    return if (count > 0) sum / count else 0f
}
