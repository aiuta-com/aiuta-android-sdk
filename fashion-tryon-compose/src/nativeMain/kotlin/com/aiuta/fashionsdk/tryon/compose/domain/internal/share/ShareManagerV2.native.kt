package com.aiuta.fashionsdk.tryon.compose.domain.internal.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utls.firstKeyWindow
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utls.isIpad
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utls.nativeLoad
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utls.watermarking
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController

internal class NativeShareManagerV2(
    private val density: Density,
    private val layoutDirection: LayoutDirection,
) : ShareManagerV2 {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun shareImages(
        content: String?,
        pageId: AiutaAnalyticsPageId,
        productIds: List<String>,
        imageUrls: List<String>,
        watermark: Painter?,
    ): Result<Unit> = runCatching {
        coroutineScope {
            val viewController = UIApplication.sharedApplication.firstKeyWindow?.rootViewController
            checkNotNull(viewController)

            val shareableImages = imageUrls.map { url ->
                async {
                    // Firstly, load
                    val image = nativeLoad(url)
                    // Try to add watermark
                    val imageWithWatermark = watermarking(
                        image = image,
                        watermark = watermark,
                        density = density,
                        layoutDirection = layoutDirection,
                    )
                    ShareableImage(image = imageWithWatermark)
                }
            }.awaitAll()

            // Now present UIActivityViewController to share
            val itemsToShare = buildList {
                addAll(shareableImages)
                content?.let { add(ShareableAddition(it)) }
            }
            val shareViewController = UIActivityViewController(itemsToShare, null)

            if (isIpad()) {
                // ipad need sourceView for show
                shareViewController.popoverPresentationController?.apply {
                    sourceView = viewController.view
                    sourceRect =
                        viewController.view.center.useContents { CGRectMake(x, y, 0.0, 0.0) }
                    permittedArrowDirections = 0uL
                }
            }

            viewController.presentViewController(
                shareViewController,
                animated = true,
                completion = null,
            )
        }
    }
}

@Composable
internal actual fun rememberActualShareManagerV2(): ShareManagerV2 {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    return remember {
        NativeShareManagerV2(
            density = density,
            layoutDirection = layoutDirection,
        )
    }
}
