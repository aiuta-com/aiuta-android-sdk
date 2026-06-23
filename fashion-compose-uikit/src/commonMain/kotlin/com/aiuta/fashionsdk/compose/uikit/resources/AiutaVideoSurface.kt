package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.compose.resources.media.AiutaMediaContentScale
import com.aiuta.fashionsdk.logger.AiutaLogger

/**
 * Displays an [AiutaMedia] surface, mirroring [AiutaImage].
 *
 * - When [AiutaMedia.videoSource] is set, the video is played by a platform
 *   player ([PlatformVideoPlayer]) with [AiutaMedia.imageResource] shown as the
 *   shutter (poster) while the video buffers and if playback fails:
 *   - Android plays via Media3/ExoPlayer
 *   - iOS plays via AVPlayer
 *   - JVM/JS/WASM render the poster image (no player)
 * - When [AiutaMedia.videoSource] is `null`, only [AiutaMedia.imageResource] is shown.
 *
 * [AiutaMedia.contentScale] drives how both the video and the poster are scaled.
 *
 * @param video The media to be displayed
 * @param modifier The modifier to be applied to the surface
 * @param autoPlay Whether playback starts automatically
 * @param loop Whether playback loops
 * @param muted Whether audio is muted
 * @param logger Optional logger used to report playback failures
 */
@Composable
public fun AiutaVideoSurface(
    video: AiutaMedia,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    loop: Boolean = true,
    muted: Boolean = true,
    logger: AiutaLogger? = null,
) {
    val contentScale = video.contentScale.toContentScale()

    val videoSource = video.videoSource
    if (videoSource.isNullOrEmpty()) {
        // No video — show the image as the content.
        AiutaImage(
            image = video.imageResource,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
        )
    } else {
        PlatformVideoPlayer(
            source = videoSource,
            modifier = modifier,
            contentScale = contentScale,
            autoPlay = autoPlay,
            loop = loop,
            muted = muted,
            logger = logger,
            shutter = {
                AiutaImage(
                    image = video.imageResource,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale,
                )
            },
        )
    }
}

/** Maps the SDK's [AiutaMediaContentScale] to a Compose [ContentScale]. */
internal fun AiutaMediaContentScale.toContentScale(): ContentScale = when (this) {
    AiutaMediaContentScale.FIT -> ContentScale.Fit
    AiutaMediaContentScale.FILL -> ContentScale.Crop
}

/**
 * Platform player backing [AiutaVideoSurface]. Receives a playable [source]
 * string (a remote URL or a local file URI) and a [shutter] poster shown while
 * the video is not yet rendering / on failure. [logger] reports playback failures.
 */
@Composable
internal expect fun PlatformVideoPlayer(
    source: String,
    modifier: Modifier,
    contentScale: ContentScale,
    autoPlay: Boolean,
    loop: Boolean,
    muted: Boolean,
    logger: AiutaLogger?,
    shutter: @Composable () -> Unit,
)
