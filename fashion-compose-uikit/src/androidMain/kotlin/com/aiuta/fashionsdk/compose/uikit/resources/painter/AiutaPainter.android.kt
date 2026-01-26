package com.aiuta.fashionsdk.compose.uikit.resources.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import coil3.compose.rememberAsyncImagePainter
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaAndroidDrawable
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaAndroidDrawableRes
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaComposeDrawableResource
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource

@Composable
public actual fun painterResource(drawableResource: AiutaDrawableResource): Painter = when (drawableResource) {
    // Multiplatform
    is AiutaComposeDrawableResource -> commonPainterResource(drawableResource)
    // Android
    is AiutaAndroidDrawable -> rememberAsyncImagePainter(model = drawableResource.resource)
    is AiutaAndroidDrawableRes -> painterResource(drawableResource.resource)
    // Fallback to coil
    else -> rememberAsyncImagePainter(model = drawableResource.resource)
}
