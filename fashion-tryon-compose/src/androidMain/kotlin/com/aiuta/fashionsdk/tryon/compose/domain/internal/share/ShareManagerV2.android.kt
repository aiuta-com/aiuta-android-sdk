package com.aiuta.fashionsdk.tryon.compose.domain.internal.share

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils.addWatermark
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils.getUriFromBitmap
import com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils.shareContent
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

internal class AndroidShareManagerV2(
    private val coilContext: PlatformContext,
    private val context: Context,
    private val density: Density,
    private val layoutDirection: LayoutDirection,
) : ShareManagerV2 {
    override suspend fun shareImages(
        content: String?,
        pageId: AiutaAnalyticsPageId,
        productIds: List<String>,
        imageUrls: List<String>,
        watermark: Painter?,
    ): Result<Unit> = runCatching {
        val imageUris = imageUrls
            .mapNotNull { url ->
                urlToBitmap(url)
            }
            .mapNotNull { bitmap ->
                bitmapToUri(
                    bitmap = bitmap,
                    watermarkPainter = watermark,
                )
            }

        context.shareContent(
            content = content,
            pageId = pageId,
            productIds = productIds,
            fileUris = imageUris,
        )
    }

    private suspend fun bitmapToUri(
        bitmap: Bitmap,
        watermarkPainter: Painter? = null,
    ): Uri? {
        val bitmapConfig = bitmap.config ?: return null
        return try {
            withContext(Dispatchers.Default) {
                val mutableBitmap = bitmap.copy(bitmapConfig, true)
                val modifierBitmap =
                    try {
                        watermarkPainter?.toImageBitmap()?.let { proceedWatermark ->
                            context.addWatermark(
                                source = mutableBitmap,
                                watermark = proceedWatermark,
                            )
                        } ?: mutableBitmap
                    } catch (e: Exception) {
                        // Failed to add watermark
                        mutableBitmap
                    }

                context.getUriFromBitmap(bmp = modifierBitmap, isCache = true)
            }
        } catch (e: Exception) {
            // Failed get bitmap
            coroutineContext.ensureActive()
            null
        }
    }

    private suspend fun urlToBitmap(imageUrl: String): Bitmap? = try {
        val request =
            ImageRequest.Builder(coilContext)
                .data(imageUrl)
                .allowHardware(false)
                .build()
        SingletonImageLoader.get(coilContext).execute(request).image?.toBitmap()
    } catch (e: Exception) {
        // Failed to resolve bitmap
        coroutineContext.ensureActive()
        null
    }

    private fun Painter.toImageBitmap(): ImageBitmap {
        val size = intrinsicSize
        val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        val canvas = Canvas(bitmap)
        CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
            draw(size)
        }
        return bitmap
    }
}

@Composable
internal actual fun rememberActualShareManagerV2(): ShareManagerV2 {
    val coilContext = LocalPlatformContext.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    return remember {
        AndroidShareManagerV2(
            coilContext = coilContext,
            context = context,
            density = density,
            layoutDirection = layoutDirection,
        )
    }
}
