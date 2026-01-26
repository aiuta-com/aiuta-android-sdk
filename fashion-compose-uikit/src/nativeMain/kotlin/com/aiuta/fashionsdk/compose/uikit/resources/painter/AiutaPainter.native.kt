package com.aiuta.fashionsdk.compose.uikit.resources.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import coil3.compose.rememberAsyncImagePainter
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIOSSkia

@Composable
public actual fun painterResource(drawableResource: AiutaDrawableResource): Painter {
    return when (drawableResource) {
        // Multiplatform
        is AiutaComposeDrawableResource -> commonPainterResource(drawableResource)
        // IOS
        is AiutaIOSSkia -> {
            val imageBitmap = drawableResource.resource.toComposeImageBitmap()
            return rememberAsyncImagePainter(model = imageBitmap)
        }
        else -> rememberAsyncImagePainter(model = drawableResource.resource)
    }
}
