package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller

import androidx.compose.ui.geometry.Offset
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.zoomable.toggleScale
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.deactivateTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun ZoomImageController.closeZoomImageScreen(scope: CoroutineScope) {
    scope.launch {
        imageZoomState.toggleScale(
            targetScale = 1f,
            position =
            Offset(
                x = maxContentWidth / 2f,
                y = maxContentHeight / 2f,
            ),
        )
    }
    deactivateTransition()
}
