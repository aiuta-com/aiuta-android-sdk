package com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal const val PAGE_ID_KEY = "pageIdKey"
internal const val PRODUCT_IDS_KEY = "productIdsKey"
internal const val SHARE_REQUEST_CODE = 100

@OptIn(ExperimentalTime::class)
internal fun generateImageFileName(
    extension: String = "jpeg",
) = "image_${Clock.System.now().toEpochMilliseconds()}.$extension"

internal fun Painter.toImageBitmap(
    density: Density,
    layoutDirection: LayoutDirection,
): ImageBitmap {
    val size = intrinsicSize
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)
    CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
        draw(size)
    }
    return bitmap
}
