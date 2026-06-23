package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.ContentFrame
import com.aiuta.fashionsdk.logger.AiutaLogger

@OptIn(UnstableApi::class)
@Composable
internal actual fun PlatformVideoPlayer(
    source: String,
    modifier: Modifier,
    contentScale: ContentScale,
    autoPlay: Boolean,
    loop: Boolean,
    muted: Boolean,
    logger: AiutaLogger?,
    shutter: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val exoPlayer = retain {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(source))
                repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                volume = if (muted) 0f else 1f
                playWhenReady = autoPlay
                prepare()
            }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    Box(modifier = modifier) {
        ContentFrame(
            player = exoPlayer,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            shutter = shutter,
        )
    }
}
