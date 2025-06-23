package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.list.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.SharedImageController

@Composable
internal fun rememberImageListController(
    constraints: Constraints,
) = remember(constraints) {
    ImageListController(
        constraints = constraints,
    )
}

internal class ImageListController(
    constraints: Constraints,
) : SharedImageController<ImageListUiModel>(constraints) {
    override val transitionModel: MutableState<ImageListUiModel> = mutableStateOf(ImageListUiModel.EMPTY)
}
