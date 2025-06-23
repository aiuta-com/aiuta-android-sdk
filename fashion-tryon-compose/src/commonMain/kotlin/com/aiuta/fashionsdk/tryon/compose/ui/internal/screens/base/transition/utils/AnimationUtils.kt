package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.transition.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.lerp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.utils.toIntOffset

@Composable
internal fun animateColorAsLerp(
    start: Color,
    stop: Color,
    progress: Animatable<Float, AnimationVector1D>,
): State<Color> = remember {
    derivedStateOf {
        lerp(
            start = start,
            stop = stop,
            fraction = progress.value,
        )
    }
}

@Composable
internal fun animateOffsetAsLerp(
    start: Offset,
    stop: Offset,
    progress: Animatable<Float, AnimationVector1D>,
): State<IntOffset> = remember {
    derivedStateOf {
        lerp(
            start = start,
            stop = stop,
            fraction = progress.value,
        ).toIntOffset()
    }
}

@Composable
internal fun animateSizeAsLerp(
    start: Size,
    stop: Size,
    progress: Animatable<Float, AnimationVector1D>,
): State<Size> = remember {
    derivedStateOf {
        lerp(
            start = start,
            stop = stop,
            fraction = progress.value,
        )
    }
}

@Composable
internal fun animateDpAsLerp(
    start: Dp,
    stop: Dp,
    progress: Animatable<Float, AnimationVector1D>,
): State<Dp> = remember {
    derivedStateOf {
        lerp(
            start = start,
            stop = stop,
            fraction = progress.value,
        )
    }
}
