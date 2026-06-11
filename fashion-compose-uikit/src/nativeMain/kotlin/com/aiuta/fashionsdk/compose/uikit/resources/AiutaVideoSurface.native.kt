package com.aiuta.fashionsdk.compose.uikit.resources

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.seekToTime
import platform.AVFoundation.setMuted
import platform.CoreGraphics.CGRectMake
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.UIKit.UIView
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PlatformVideoPlayer(
    source: String,
    modifier: Modifier,
    contentScale: ContentScale,
    autoPlay: Boolean,
    loop: Boolean,
    muted: Boolean,
    shutter: @Composable () -> Unit,
) {
    val player = remember(source) {
        val url = NSURL.URLWithString(source) ?: NSURL.fileURLWithPath(source)
        AVPlayer(uRL = url)
    }
    // Keep the poster visible until the player starts rendering frames.
    var isReady by remember(source) { mutableStateOf(false) }

    DisposableEffect(player, loop, muted, autoPlay) {
        player.setMuted(muted)
        if (autoPlay) player.play()

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

        // Once playback time advances, the first frame is on screen — hide the poster.
        val timeObserver = player.addPeriodicTimeObserverForInterval(
            interval = CMTimeMakeWithSeconds(0.05, 600),
            queue = dispatch_get_main_queue(),
        ) { time ->
            if (CMTimeGetSeconds(time) > 0.0) {
                isReady = true
            }
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(endObserver)
            player.removeTimeObserver(timeObserver)
            player.pause()
        }
    }

    Box(modifier = modifier) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                VideoContainerView(player).apply {
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
    player: AVPlayer,
) : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    val playerLayer: AVPlayerLayer = AVPlayerLayer().apply {
        this.player = player
    }

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
