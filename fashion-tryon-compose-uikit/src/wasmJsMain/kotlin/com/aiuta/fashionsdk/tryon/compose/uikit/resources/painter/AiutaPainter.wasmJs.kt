package com.aiuta.fashionsdk.tryon.compose.uikit.resources.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource

@Composable
public actual fun painterResource(drawableResource: AiutaDrawableResource): Painter = commonPainterResource(drawableResource)
