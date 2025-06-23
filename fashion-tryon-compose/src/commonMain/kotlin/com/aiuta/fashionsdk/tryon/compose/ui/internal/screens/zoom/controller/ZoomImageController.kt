package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.zoom.ZoomImageUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.ZoomState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.rememberZoomState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.SharedImageController

@Composable
internal fun rememberZoomImageController(
    constraints: Constraints,
    imageZoomState: ZoomState = rememberZoomState(),
) = remember(constraints) {
    ZoomImageController(
        constraints = constraints,
        imageZoomState = imageZoomState,
    )
}

@Immutable
internal class ZoomImageController(
    constraints: Constraints,
    public val imageZoomState: ZoomState,
) : SharedImageController<ZoomImageUiModel>(constraints) {

    override val transitionModel: MutableState<ZoomImageUiModel> = mutableStateOf(ZoomImageUiModel.EMPTY)
}
