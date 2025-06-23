package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.SharedImageController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.disableSharingState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.isTransitionActive
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller.isTransitionActiveListener
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.TRANSITION_ANIM_DURATION
import kotlinx.coroutines.launch

@Composable
internal fun <T : SharedImageTransitionModel> BaseSharedImageScreen(
    controller: SharedImageController<T>,
    content: @Composable (sharedElementProgress: Animatable<Float, AnimationVector1D>) -> Unit,
) {
    val isTransitionActive = controller.isTransitionActiveListener()
    val sharedElementProgress = remember { Animatable(if (isTransitionActive.value) 0f else 1f) }

    LaunchedEffect(isTransitionActive) {
        launch {
            sharedElementProgress.animateTo(
                targetValue = if (isTransitionActive.value) 1f else 0f,
                animationSpec = tween(durationMillis = TRANSITION_ANIM_DURATION),
            )
            if (!controller.isTransitionActive()) {
                controller.disableSharingState()
            }
        }
    }

    content(sharedElementProgress)
}
