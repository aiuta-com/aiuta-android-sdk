package com.aiuta.fashionsdk.compose.resources.media

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource

/**
 * A media resource for the Aiuta SDK that combines a static image with an
 * optional video.
 *
 * Use it wherever a surface may show either a looping video or a still image:
 * - [imageResource] is always required. It is shown as the poster/placeholder
 *   while the video buffers, if playback fails, and as the sole content when
 *   [videoSource] is `null`.
 * - [videoSource] is an optional playable source string — a remote `https://`
 *   URL or a local file URI (e.g. resolved via a module's `Res.getUri(...)`).
 *   When set, the video is played on top of [imageResource].
 * - [contentScale] controls how both the image and the video are scaled to fit
 *   the surface bounds.
 *
 * ```kotlin
 * // Image + looping bundled video
 * AiutaMedia(
 *     imageResource = AiutaComposeDrawableResource(Res.drawable.poster),
 *     videoSource = Res.getUri("files/promo.mp4"),
 *     contentScale = AiutaMediaContentScale.FILL,
 * )
 *
 * // Image only
 * AiutaMedia(
 *     imageResource = AiutaComposeDrawableResource(Res.drawable.poster),
 *     contentScale = AiutaMediaContentScale.FIT,
 * )
 * ```
 *
 * @property imageResource Poster/placeholder image, and the content when no video is set
 * @property videoSource Optional video source — a remote URL or a local file URI
 * @property contentScale How the image and video are scaled within the surface
 */
@Immutable
public class AiutaMedia(
    public val imageResource: AiutaDrawableResource,
    public val videoSource: String? = null,
    public val contentScale: AiutaMediaContentScale = AiutaMediaContentScale.FILL,
)

/**
 * Scaling strategy for [AiutaMedia] content within its surface bounds.
 */
public enum class AiutaMediaContentScale {
    /** Scale to fit entirely within the bounds, preserving aspect ratio (may letterbox). */
    FIT,

    /** Scale to fill the bounds, preserving aspect ratio and cropping the overflow. */
    FILL,
}
