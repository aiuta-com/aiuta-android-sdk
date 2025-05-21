package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun AiutaLoadingComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    circleSize: Dp = 14.dp,
    circleColor: Color = LocalTheme.current.color.brand,
    component: @Composable () -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = isLoading,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentAlignment = Alignment.Center,
    ) { isOperationLoading ->
        if (isOperationLoading) {
            AiutaLoadingIcon(
                modifier = Modifier.size(circleSize),
                circleColor = circleColor,
            )
        } else {
            component()
        }
    }
}
