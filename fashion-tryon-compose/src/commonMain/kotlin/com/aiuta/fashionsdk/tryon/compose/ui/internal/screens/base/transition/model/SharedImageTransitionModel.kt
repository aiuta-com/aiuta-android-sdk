package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp

internal interface SharedImageTransitionModel {
    val imageSize: Size
    val initialCornerRadius: Dp
    val parentImageOffset: Offset
}
