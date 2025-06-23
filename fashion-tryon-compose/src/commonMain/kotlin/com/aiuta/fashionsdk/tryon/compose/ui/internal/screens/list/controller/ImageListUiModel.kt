package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.controller

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionModel

@Immutable
internal class ImageListUiModel(
    override val imageSize: Size,
    override val initialCornerRadius: Dp,
    override val parentImageOffset: Offset,
//    val imageUrl: String?,
) : SharedImageTransitionModel {
    companion object {
        val EMPTY by lazy {
            ImageListUiModel(
                imageSize = Size.Unspecified,
                initialCornerRadius = 0.dp,
                parentImageOffset = Offset.Unspecified,
            )
        }
    }
}
