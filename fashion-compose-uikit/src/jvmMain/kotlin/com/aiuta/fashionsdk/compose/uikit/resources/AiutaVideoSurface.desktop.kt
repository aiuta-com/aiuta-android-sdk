package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.aiuta.fashionsdk.logger.AiutaLogger

// Desktop/JVM has no bundled video player; show the poster image.
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
    Box(modifier = modifier) {
        shutter()
    }
}
