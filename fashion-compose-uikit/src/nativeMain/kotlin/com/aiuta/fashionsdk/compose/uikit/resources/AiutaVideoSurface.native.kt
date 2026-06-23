package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.UIKitView
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.e
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.delay
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemStatusFailed
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.currentItem
import platform.AVFoundation.error
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.AVFoundation.setMuted
import platform.AVFoundation.status
import platform.CoreGraphics.CGRectMake
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
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
    val player = remember(source) {
        val url = NSURL.URLWithString(source) ?: NSURL.fileURLWithPath(source)
        AVPlayer(uRL = url)
    }
    // Hoisted so we can observe when its first frame is on screen.
    val playerLayer = remember(player) {
        AVPlayerLayer().apply { this.player = player }
    }
    // Keep the poster visible until the player has rendered its first frame.
    var isReady by remember(source) { mutableStateOf(false) }

    // Start playback only once the item is ready. For remote URLs the item loads
    // asynchronously and is still `.unknown` right after creation — calling play()
    // too early is dropped by AVPlayer, leaving the video frozen on its first frame.
    // This mirrors ExoPlayer's `playWhenReady`, which starts when the item is ready.
    LaunchedEffect(player, autoPlay) {
        if (!autoPlay) return@LaunchedEffect
        while (true) {
            when (player.currentItem?.status) {
                AVPlayerItemStatusReadyToPlay -> {
                    player.play()
                    break
                }
                AVPlayerItemStatusFailed -> {
                    logger?.e(
                        "AiutaVideoSurface: item failed to load: " +
                            "${player.currentItem?.error}",
                    )
                    break
                }
                else -> delay(50)
            }
        }
    }

    // Hide the poster only once the layer actually has a frame to display, so the
    // poster swaps straight to moving video with no blank gap in between. This is
    // the AVPlayerLayer equivalent of media3 ContentFrame's shutter on Android.
    LaunchedEffect(playerLayer) {
        while (!playerLayer.isReadyForDisplay()) delay(16)
        isReady = true
    }

    DisposableEffect(player, loop, muted) {
        player.setMuted(muted)

        val endObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) { _ ->
            if (loop) {
                player.seekToTime(CMTimeMake(value = 0, timescale = 1))
                player.play()
            }
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(endObserver)
            player.pause()
        }
    }

    Box(modifier = modifier) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                VideoContainerView(playerLayer).apply {
                    playerLayer.videoGravity = contentScale.toVideoGravity()
                }
            },
            update = { view ->
                view.playerLayer.videoGravity = contentScale.toVideoGravity()
            },
        )

        if (!isReady) {
            Box(modifier = Modifier.fillMaxSize()) {
                shutter()
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class VideoContainerView(
    val playerLayer: AVPlayerLayer,
) : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    init {
        layer.addSublayer(playerLayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        // Disable implicit animations so the layer tracks the view bounds instantly.
        CATransaction.begin()
        CATransaction.setDisableActions(true)
        playerLayer.setFrame(bounds)
        CATransaction.commit()
    }
}

private fun ContentScale.toVideoGravity(): String? = when (this) {
    ContentScale.Crop -> AVLayerVideoGravityResizeAspectFill
    ContentScale.FillBounds -> AVLayerVideoGravityResize
    else -> AVLayerVideoGravityResizeAspect
}
