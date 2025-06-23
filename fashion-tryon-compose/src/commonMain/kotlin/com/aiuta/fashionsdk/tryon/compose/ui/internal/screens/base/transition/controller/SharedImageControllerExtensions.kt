package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.model.SharedImageTransitionState

// State
internal fun <T : SharedImageTransitionModel> SharedImageController<T>.enableSharingState() = changeSharingState(
    SharedImageState.ENABLE,
)

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.disableSharingState() = changeSharingState(
    SharedImageState.DISABLE,
)

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.isSharingEnable() = state.value == SharedImageState.ENABLE

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.changeSharingState(newState: SharedImageState) {
    state.value = newState
}

// Transition
internal fun <T : SharedImageTransitionModel> SharedImageController<T>.activateTransition() = changeTransitionState(
    SharedImageTransitionState.ACTIVE,
)

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.deactivateTransition() = changeTransitionState(
    SharedImageTransitionState.IDLE,
)

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.isTransitionActive() = transitionState.value == SharedImageTransitionState.ACTIVE

@Composable
internal fun <T : SharedImageTransitionModel> SharedImageController<T>.isTransitionActiveListener() = remember(transitionState.value) {
    derivedStateOf {
        isTransitionActive()
    }
}

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.changeTransitionState(newTransitionState: SharedImageTransitionState) {
    transitionState.value = newTransitionState
}

// Shared data
internal fun <T : SharedImageTransitionModel> SharedImageController<T>.setActiveSharedImageModel(newModel: T) {
    transitionModel.value = newModel
}

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.openScreen(model: T) {
    setActiveSharedImageModel(model)
    activateTransition()
    enableSharingState()
}

internal fun <T : SharedImageTransitionModel> SharedImageController<T>.closeScreen() {
    deactivateTransition()
}
