package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Constraints
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionState

@Immutable
internal abstract class SharedImageController<T : SharedImageTransitionModel>(
    constraints: Constraints,
) {
    public val maxContentWidth = constraints.maxWidth
    public val maxContentHeight = constraints.maxHeight
    public val maxSize by lazy {
        Size(
            width = maxContentWidth.toFloat(),
            height = maxContentHeight.toFloat(),
        )
    }

    public val state: MutableState<SharedImageState> = mutableStateOf(SharedImageState.DISABLE)
    public val transitionState: MutableState<SharedImageTransitionState> = mutableStateOf(
        SharedImageTransitionState.IDLE,
    )

    public abstract val transitionModel: MutableState<T>
}
